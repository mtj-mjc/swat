
package ch.mrnjec.swat.micro;

import io.micronaut.runtime.Micronaut;

/**
 * Applikationskontext für micronaut.io.
 */
public class Application {

    /**
     * Privater Konstruktor.
     */
    private Application() {
    }

    /**
     * Startet den Service.
     * @param args not used.
     */
    public static void main(final String[] args) {
        Micronaut.run(Application.class);
    }
}