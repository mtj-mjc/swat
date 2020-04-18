package ch.mrnjec.swat.mongo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This Class reads out the MongoDB configurations.
 *
 * @since: 10.05.2019
 * @author: Matej Mrnjec
 */
public class MongoDbConfig {

    private static final Logger LOG = LogManager.getLogger(MongoDbConfig.class);
    private static final String CONNECTION_ADDRESS = "connection";
    private static final String CONFIG_FILE_NAME = "mongodb.properties";

    // MongoDB Values
    public static final String DATABASE = "user_management";
    public static final String GROUPS_COLLECTION = "groups";
    public static final String USERS_COLLECTION = "users";
    public static final String STORES_COLLECTION = "stores";

    // Error Messages
    public static final String UPDATE_NOT_ACKNOWLEDGED = "Update was not acknowledged from the Database";
    public static final String REMOVE_NOT_ACKNOWLEDGED = "Remove was not acknowledged from the Database";

    private final Properties properties = new Properties();

    /**
     * Liest die Konfiguration vom Default-File ein.
     */
    public MongoDbConfig() {
        this(CONFIG_FILE_NAME);
    }

    /**
     * Liest die Konfiguration ein.
     * @param fileName Dateiname.
     */
    MongoDbConfig(final String fileName) {
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
     * @return Liefert das Attribut connection.
     */
    String getConnectionAddress() {
        return this.properties.getProperty(CONNECTION_ADDRESS);
    }
}
