package ch.mrnjec.swat.micro;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Demo für Applikationsstart.
 */
public final class Application {

    /**
     * TimerTask für periodische Ausführung.
     */
    private static final class HeartBeat extends TimerTask {

        private static final Logger LOG = LogManager.getLogger(HeartBeat.class);

        private ServiceTemplate service;

        HeartBeat() {
            try {
                this.service = new ServiceTemplate();
            } catch (IOException | TimeoutException e) {
                LOG.error(e);
            }
        }

        @Override
        public void run() {
            try {
                service.registerStudent();
            } catch (IOException | InterruptedException e) {
                LOG.error(e);
            }
        }
    }

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
        final Timer timer = new Timer();
        timer.schedule(new HeartBeat(), 0, 5000);
    }
}
