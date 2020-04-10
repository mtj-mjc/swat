package ch.mrnjec.swat.bus;

import java.io.IOException;

/**
 * Listener Interface für Messages aus RabbitMQ.
 */
public interface MessageReceiver {

    /**
     * Listener Methode für Messages.
     * @param message Message.
     * @param replyTo ReplyTo address.
     * @param correlationId CorrelationId.
     * @throws IOException
     */
    void onMessageReceived(String message, String replyTo, String correlationId) throws IOException;
}
