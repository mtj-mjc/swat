package ch.mrnjec.swat.bus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.concurrent.TimeoutException;


@Singleton
public class RabbitMQCommunication implements Communication {

    private final String exchangeName;
    private final BusConnector bus;

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQCommunication.class);

    public RabbitMQCommunication() throws IOException, TimeoutException {
        this.exchangeName = new RabbitMqConfig().getExchange();
        this.bus = new BusConnector();
        this.bus.connect();
        LOG.info("connected to bus");

    }

    public String syncCall(final String route, final String message) throws IOException, InterruptedException {
        return bus.talkSync(this.exchangeName, route, message);
    }
}
