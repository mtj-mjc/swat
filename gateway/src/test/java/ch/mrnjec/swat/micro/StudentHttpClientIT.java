
package ch.mrnjec.swat.micro;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.mrnjec.swat.entities.Student;

/**
 * Testcases für Student Service. Verwendet Java 11 HttpClient.
 */
final class StudentHttpClientIT {

    private static final String BASE_URL = "http://localhost:8090/api/v1/students";


    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);

    @Test
    @Disabled
    void testStudentGetOne() throws Exception {
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/1")).GET().build();
        final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"id\":1");
        assertThat(response.body()).contains("\"lastName\":\"Grone\"");
    }

    @Test
    @Disabled
    void testStudentGetTwoAsJson() throws Exception {
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/2")).GET().build();
        final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        final String json = response.body();
        final Student student = mapper.readValue(json, Student.class);
        assertThat(student.getId()).isEqualTo(2);
        assertThat(student.getFirstName()).isEqualTo("Babette");
        assertThat(student.getLastName()).isEqualTo("Zweifel");
        assertThat(student.getMonthOfBirth()).isEqualTo(4);
    }

    @Test
    @Disabled
    void testStudentGetWhichNotExist() throws Exception {
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/999999")).GET().build();
        final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(404);
    }

    @Test
    @Disabled
    void testStudentGetByLastName() throws Exception {
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/?lastname=Zweifel"))
                    .GET().build();
        final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"lastName\":\"Zweifel\"");
    }

    @Test
    @Disabled
    void testStudentDeleteTree() throws Exception {
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/3")).DELETE().build();
        final HttpResponse<?> response = httpClient.send(request, BodyHandlers.discarding());
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @Disabled
    void testStudentCreate() throws Exception {
        final Student student = new Student("Vorname", "Nachname", 8);
        final String jsonString = mapper.writeValueAsString(student);
        System.out.println(jsonString);
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/"))
                    .POST(BodyPublishers.ofString(jsonString))
                    .build();
        final HttpResponse<?> response = httpClient.send(request, BodyHandlers.discarding());
        assertThat(response.statusCode()).isEqualTo(200);
    }
}
