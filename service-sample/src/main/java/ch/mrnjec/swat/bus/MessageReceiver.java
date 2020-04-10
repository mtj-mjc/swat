package ch.mrnjec.swat.bus;

/**
 * Listener Interface für Messages aus RabbitMQ.
 */
public interface MessageReceiver {

    /**
     * Listener Methode für Messages.
     * @param route Route.
     * @param message Message.
     */
    void onMessageReceived(String route, String message);
}
