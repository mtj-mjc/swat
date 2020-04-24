package ch.mrnjec.swat.bus;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Beispielcode für Verbindung mit RabbitMQ.
 */
public final class BusConnector implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(BusConnector.class);

    // connection to bus
    private Connection connection;

    // use different channels for different threads
    private Channel channelTalk;
    private Channel channelListen;

    /**
     * Beispiel für asynchrone Kommunikation (Send).
     * @param exchange Exchange.
     * @param route Route.
     * @param message Response.
     * @throws IOException Exception.
     */
    public void talkAsync(final String exchange, final String route, final String message) throws IOException {
        AMQP.BasicProperties props = new AMQP.BasicProperties();
        channelTalk.basicPublish(exchange, route, props, message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * asynchrone Kommunikation (Send) mit correlation id.
     * @param exchange Exchange.
     * @param route Route.
     * @param message Message.
     * @param correlationId Correlation ID.
     * @throws IOException Exception.
     */
    public void talkAsync(final String exchange, final String route, final String message, String correlationId) throws IOException {
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(correlationId)
                .build();
        channelTalk.basicPublish(exchange, route, props, message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Beispiel für synchrone Kommunikation.
     * @param exchange Exchange.
     * @param route Route.
     * @param message Response.
     * @return String.
     * @throws IOException Exception.
     * @throws InterruptedException Exception.
     */
    public String talkSync(final String exchange, final String route, final String message)
                throws IOException, InterruptedException {

        // create a temporary reply queue
        final String corrId = UUID.randomUUID().toString();
        final String replyQueueName = channelTalk.queueDeclare().getQueue();
        channelTalk.queueBind(replyQueueName, exchange, replyQueueName);

        // setup receiver
        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
        final String consumerId = channelTalk.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {

            // check if response matches correlation id
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                boolean isInserted = response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
                if(!isInserted)
                    LOG.info("No space available");
            }
        }, consumerTag -> {
            // empty
        });

        // send message
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName)
                    .build();
        channelTalk.basicPublish(exchange, route, props, message.getBytes(StandardCharsets.UTF_8));

        // receive message with timeout
        final String result = response.poll(5, TimeUnit.SECONDS);
        channelTalk.basicCancel(consumerId);
        return result;

    }

    /**
     * Beispiel für Listener (asynchroner Empfang).
     * @param exchange Exchange.
     * @param queueName Queue.
     * @param route Route.
     * @param receiver Empfänger.
     * @throws IOException IOException.
     */
    public void listenFor(final String exchange, final String queueName, final String route,
                final MessageReceiver receiver) throws IOException {

        // create queue to receive messages
        channelListen.queueDeclare(queueName, true, false, true, new HashMap<>());
        channelListen.queueBind(queueName, exchange, route);

        // add listener
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String replyTo = delivery.getProperties().getReplyTo();
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            receiver.onMessageReceived(route, message, replyTo);
        };
        channelListen.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            // empty
        });
    }

    /**
     * Öffnet die Verbindung zu RabbitMQ.
     * @throws IOException IOException.
     * @throws TimeoutException TimeoutException.
     */
    public void connect() throws IOException, TimeoutException {

        // retrieve configuration
        RabbitMqConfig config = new RabbitMqConfig();

        // create connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.getHost());
        factory.setUsername(config.getUsername());
        factory.setPassword(config.getPassword());
        LOG.debug("Connecting to {}...", config.getHost());
        this.connection = factory.newConnection();

        // create channels within connection
        this.channelTalk = connection.createChannel();
        this.channelListen = connection.createChannel();
        LOG.debug("Successfully connected to {}...", config.getHost());
    }

    /**
     * Schliesst die Verbindung zu RabbitMQ.
     * @see AutoCloseable#close()
     */
    @Override
    public void close() {
        try {
            channelTalk.close();
            channelListen.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            LOG.error(e.getMessage());
        }
    }
}
