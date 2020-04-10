package ch.mrnjec.swat.bus;

import java.io.IOException;

/**
 * Listener Interface für Messages aus RabbitMQ.
 */
public interface MessageReceiver {

    /**
     * Listener Methode für Messages.
     * @param route Route.
     * @param message Response.
     */
    void onMessageReceived(String route, String message, String replyTo) throws IOException;
}
