package ch.mrnjec.swat.bus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Liest Konfiguration für RabbitMQ aus Propertyfile.
 */
final class RabbitMqConfig {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMqConfig.class);
    private static final String EXCHANGE = "exchange";
    private static final String PASSWORD = "password";
    private static final String USER = "user";
    private static final String HOST = "host";
    private static final String CONFIG_FILE_NAME = "rabbitmq.properties";

    private final Properties properties = new Properties();

    /**
     * Liest die Konfiguration vom Default-File ein.
     */
    public RabbitMqConfig() {
        this(CONFIG_FILE_NAME);
    }

    /**
     * Liest die Konfiguration ein.
     * @param fileName Dateiname.
     */
    RabbitMqConfig(final String fileName) {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(inputStream);
            assert inputStream != null;
            inputStream.close();
        } catch (IOException e) {
            LOG.error("Error while reading from file {}", CONFIG_FILE_NAME);
        }
    }

    /**
     * @return Liefert das Attribut host.
     */
    String getHost() {
        return this.properties.getProperty(HOST);
    }

    /**
     * @return Liefert das Attribut user.
     */
    String getUsername() {
        return this.properties.getProperty(USER);
    }

    /**
     * @return Liefert das Attribut password.
     */
    String getPassword() {
        return this.properties.getProperty(PASSWORD);
    }

    /**
     * @return Liefert das Attribut exchange.
     */
    public String getExchange() {
        return this.properties.getProperty(EXCHANGE);
    }
}
