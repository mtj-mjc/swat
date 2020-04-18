package ch.mrnjec.swat.micro;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Demo für Applikationsstart.
 */
public final class Application {

    /**
     * Privater Konstruktor.
     */
    private Application() {
    }

    /**
     * main-Methode. Startet einen Timer für den HeartBeat.
     * @param args not used.
     */
    public static void main(final String[] args) {
        final Logger log = LogManager.getLogger(Application.class);
        try (Service service = new Service()) {
            log.info("Started Service...");
        } catch (IOException | TimeoutException e) {
            log.error(e);
        }
    }
}
