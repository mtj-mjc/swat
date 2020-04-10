package ch.mrnjec.swat.micro;


import ch.mrnjec.swat.bus.Communication;
import ch.mrnjec.swat.bus.RabbitMQCommunication;
import ch.mrnjec.swat.bus.Response;
import ch.mrnjec.swat.bus.Status;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
class OrderControllerTest {


    @Inject
    Communication communication;

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    void testGetOrders() throws IOException, TimeoutException, InterruptedException {
        Response response = new Response(Status.OK, "orders", "");
        ObjectMapper mapper = new ObjectMapper();
        String responseString = mapper.writeValueAsString(response);
        when(communication.syncCall(eq("order.list"), anyString())).thenReturn(responseString);
        final HttpRequest<String> request = HttpRequest.GET("/api/v1/order/");
        final String body = client.toBlocking().retrieve(request);
        assertThat(body).isEqualTo("orders");
    }

    @Test
    void testOrderCreateException() throws IOException, InterruptedException, TimeoutException {
        when(communication.syncCall(anyString(), anyString())).thenThrow(new IOException("Test Error"));
        String input = "{\"id\":1,\"products\":[{\"id\":1,\"name\":\"Product 1\",\"price\":3.5,\"description\":\"Description\",\"category\":{\"id\":1,\"name\":\"elektronik\"}},{\"id\":2,\"name\":\"Product 2\",\"price\":3.5,\"description\":\"Description\",\"category\":{\"id\":1,\"name\":\"elektronik\"}}],\"username\":\"3\",\"customerId\":\"4\",\"time\":\"20.03.2019 9:30\"}";
        final HttpRequest<String> request = HttpRequest.PUT("/api/v1/order/", input);
        try {
            client.toBlocking().exchange(request);
            fail("should have thrown error");
        } catch (HttpClientResponseException ex) {
            assertThat(ex.getStatus().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
        }
    }

    @Test
    void testOrderCreateProductsUnavailable() throws IOException, InterruptedException, TimeoutException {
        String data = "Products unavaliable";
        Response response = new Response(Status.PRODUCTS_NOT_AVAILABLE, data, "");
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(response);

        when(communication.syncCall(anyString(), anyString())).thenReturn(jsonResponse);
        String input = "{\"id\":1,\"products\":[{\"id\":1,\"name\":\"Product 1\",\"price\":3.5,\"description\":\"Description\",\"category\":{\"id\":1,\"name\":\"elektronik\"}},{\"id\":2,\"name\":\"Product 2\",\"price\":3.5,\"description\":\"Description\",\"category\":{\"id\":1,\"name\":\"elektronik\"}}],\"username\":\"3\",\"customerId\":\"4\",\"time\":\"20.03.2019 9:30\"}";
        final HttpRequest<String> request = HttpRequest.PUT("/api/v1/order/", input);
        try {
            client.toBlocking().exchange(request);
            fail("no exception thrown");
        } catch (HttpClientResponseException ex) {
            assertThat(ex.getStatus().getCode()).isEqualTo(416);
            assertThat(ex.getMessage()).isEqualTo("Products not existing or not available");
        }
    }

    @Test
    void testOrderCreate() throws IOException, InterruptedException, TimeoutException {
        String data = "Order erfolgreich erstellt";
        Response response = new Response(Status.OK, data, "");
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(response);
        when(communication.syncCall(anyString(), anyString())).thenReturn(jsonResponse);
        String input = "{\"id\":1,\"products\":[{\"id\":1,\"name\":\"Product 1\",\"price\":3.5,\"description\":\"Description\",\"category\":{\"id\":1,\"name\":\"elektronik\"}},{\"id\":2,\"name\":\"Product 2\",\"price\":3.5,\"description\":\"Description\",\"category\":{\"id\":1,\"name\":\"elektronik\"}}],\"username\":\"3\",\"customerId\":\"4\",\"time\":\"20.03.2019 9:30\"}";
        final HttpRequest<String> request = HttpRequest.PUT("/api/v1/order/", input);
        try {
            final HttpResponse<String> httpResponse = client.toBlocking().exchange(request);
            assertThat(httpResponse.getStatus().getCode()).isEqualTo(200);
        } catch (HttpClientResponseException ex) {
            fail("thrown Exception: " + ex.getMessage());
        }
    }

    @Test
    void testOrderGetCompletedByUser() throws IOException, TimeoutException, InterruptedException {
        String orders = "";
        Response response = new Response(Status.OK, orders, "");
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(response);
        when(communication.syncCall(anyString(), anyString())).thenReturn(jsonResponse);
        final HttpRequest<String> request = HttpRequest.GET("/api/v1/order/user?username=user");
        try {
            final HttpResponse<String> httpResponse = client.toBlocking().exchange(request);
            assertThat(httpResponse.getStatus().getCode()).isEqualTo(200);
        } catch (HttpClientResponseException ex) {
            fail("thrown Exception " + ex.getMessage());
        }
    }

    @MockBean(RabbitMQCommunication.class)
    Communication communication() {
        return mock(Communication.class);
    }
}