package ch.mrnjec.swat.bus;

import com.rabbitmq.client.impl.AMQBasicProperties;

import java.io.IOException;

/**
 * Listener Interface für Messages aus RabbitMQ.
 * Mittels lambda expression die callback Methode angeben
 */
public interface MessageReceiver {
    /**
     * Listener Methode für Messages.*
     * @param message Message.
     * @param replyTo ReplyTo address.
     * @param correlationId CorrelationId.
     * @throws IOException
     */
    void onMessageReceived(String message, String replyTo, String correlationId) throws IOException;
}

