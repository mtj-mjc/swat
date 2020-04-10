
package ch.mrnjec.swat.micro;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;

/**
 * Testcases für Student Service. Verwendet Micronaut.io.
 */
final class StudentMicronautIT {

    private static EmbeddedServer server;
    private static HttpClient client;

    @BeforeAll
    static void setupServer() {
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server.getApplicationContext().createBean(HttpClient.class, server.getURL());
    }

    @AfterAll
    static void stopServer() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

    @Test
    @Disabled
    public void testGetStudentOne() throws Exception {
        final HttpRequest<String> request = HttpRequest.GET("/api/v1/students/1");
        final String body = client.toBlocking().retrieve(request);
        Assertions.assertThat(body).contains("\"lastName\":\"Grone\"");
    }

    @Test
    @Disabled
    public void testGetStudentTwo() throws Exception {
        final HttpRequest<String> request = HttpRequest.GET("/api/v1/students/2");
        final String body = client.toBlocking().retrieve(request);
        Assertions.assertThat(body).contains("\"lastName\":\"Zweifel\"");
    }

    @Test
    @Disabled
    public void testStudentSearch() throws Exception {
        final HttpRequest<String> request = HttpRequest.GET("/api/v1/students?lastname=Zweifel");
        final String body = client.toBlocking().retrieve(request);
        Assertions.assertThat(body).contains("\"lastName\":\"Zweifel\"");
    }
}
