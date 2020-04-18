package ch.mrnjec.swat.bus;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Test method for {@link RabbitMqConfig}.
 */
final class RabbitMqConfigTest {
    private static final String RABBITMQ_PROPERTIES = "rabbitmq.test.properties"

    /**
     * Test method for {@link RabbitMqConfig#getHost()}.
     */
    @Test
    void testGetHost() {
        assertThat(new RabbitMqConfig(RABBITMQ_PROPERTIES).getHost()).isEqualTo("1111");
    }

    /**
     * Test method for {@link RabbitMqConfig#getUsername()}.
     */
    @Test
    void testGetUsername() {
        assertThat(new RabbitMqConfig(RABBITMQ_PROPERTIES).getUsername()).isEqualTo("2222");
    }

    /**
     * Test method for {@link RabbitMqConfig#getPassword()}.
     */
    @Test
    void testGetPassword() {
        assertThat(new RabbitMqConfig(RABBITMQ_PROPERTIES).getPassword()).isEqualTo("3333");
    }

    /**
     * Test method for {@link RabbitMqConfig#getExchange()}.
     */
    @Test
    void testGetExchange() {
        assertThat(new RabbitMqConfig(RABBITMQ_PROPERTIES).getExchange()).isEqualTo("4444");
    }

}
