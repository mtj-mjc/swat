
package ch.mrnjec.swat.micro;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.mrnjec.swat.entities.Student;

/**
 * Testcases für Student Service. Verwendet TestContainer (Docker).
 */
@Testcontainers
@Disabled
final class StudentTestContainerIT {

    @Container
    private static final GenericContainer<?> CONTAINER = new GenericContainer<>("appe/gateway-sample:latest")
                .withExposedPorts(8090);

    @Test
    @Disabled
    void testGetStudentTwoJson() throws IOException, InterruptedException {
        final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final String url = String.format("http://%s:%d/api/v1/students/%d", CONTAINER.getContainerIpAddress(),
                    CONTAINER.getMappedPort(8090), 2);
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        final String json = response.body();
        final Student student = mapper.readValue(json, Student.class);
        assertAll("Student", () -> assertThat(student.getId()).isEqualTo(2),
                    () -> assertThat(student.getFirstName()).isEqualTo("Babette"),
                    () -> assertThat(student.getLastName()).isEqualTo("Zweifel"),
                    () -> assertThat(student.getMonthOfBirth()).isEqualTo(4));
    }
}
