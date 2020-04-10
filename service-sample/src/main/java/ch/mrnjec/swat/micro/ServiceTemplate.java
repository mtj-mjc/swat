package ch.mrnjec.swat.micro;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

import ch.mrnjec.swat.bus.MessageReceiver;
import ch.mrnjec.swat.entities.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.mrnjec.swat.bus.BusConnector;
import ch.mrnjec.swat.bus.RabbitMqConfig;

/**
 * Beispielcode für Implementation eines Servcies mit RabbitMQ.
 */
public final class ServiceTemplate implements AutoCloseable, MessageReceiver {

    private static final Logger LOG = LogManager.getLogger(ServiceTemplate.class);

    private static final String ROUTE_STUDENT_REGISTER = "student.register";
    private static final String ROUTE_STUDENT_LIST = "student.list";
    private static final String ROUTE_REGISTRY_CHANGED = "registry.changed";
    private static final String ROUTE_STATISTICS_CHANGED = "statistics.changed";

    private final String exchangeName;
    private final BusConnector bus;

    /**
     * @throws IOException
     * @throws TimeoutException
     */
    ServiceTemplate() throws IOException, TimeoutException {
        this.exchangeName = new RabbitMqConfig().getExchange();
        this.bus = new BusConnector();
        this.bus.connect();
        this.receiveStatisticsChange();
    }

    /**
     * Erzeugt eine Student-Entity und sendet einen Event.
     * @return Student.
     * @throws IOException IOException.
     * @throws InterruptedException InterruptedException.
     */
    public Student registerStudent() throws IOException, InterruptedException {

        // create new student
        final Student student = new Student(1, "John", "Doe", ThreadLocalRandom.current().nextInt(1, 13));

        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(student);

        // send message to register student in registry, sync communication (awaiting response)
        LOG.debug("Sending synchronous message to broker with routing [{}]", ROUTE_STUDENT_REGISTER);
        String response = bus.talkSync(exchangeName, ROUTE_STUDENT_REGISTER, data);

        // receive new student object
        if (response == null) {
            LOG.debug("Received no response. Timeout occurred. Will retry later");
            return null;
        }
        LOG.debug("Received response: [{}]", response);
        Student registeredStudent = mapper.readValue(response, Student.class);

        // notify universe about new student
        // NOTE: should actually be responsibility of registry-service. Was put here to provide code
        // examples.
        this.broadcastRegistryChange();

        // return new object
        return registeredStudent;
    }

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    private void broadcastRegistryChange() throws IOException, InterruptedException {

        // request all students, sync communication (awaiting response)
        LOG.debug("Sending synchronous message to broker with routing [{}]", ROUTE_STUDENT_LIST);
        String response = bus.talkSync(exchangeName, ROUTE_STUDENT_LIST, "");

        // extract response
        if (response == null) {
            LOG.debug("Received no response. Timeout occurred. Will retry later");
            return;
        }
        LOG.debug("Received response to [{}]", ROUTE_STUDENT_LIST);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Student[] studentList = mapper.readValue(response, Student[].class);

        // broadcast student list as event, async communication
        String data = mapper.writeValueAsString(studentList);
        LOG.debug("Sending asynchronous message to broker with routing [{}]", ROUTE_REGISTRY_CHANGED);
        bus.talkAsync(exchangeName, ROUTE_REGISTRY_CHANGED, data);
    }

    /**
     * @throws IOException
     */
    private void receiveStatisticsChange() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", ROUTE_STATISTICS_CHANGED);
        bus.listenFor(exchangeName, "ServiceTemplate | " + ROUTE_STATISTICS_CHANGED, ROUTE_STATISTICS_CHANGED, this);
    }

    /**
     * @see MessageReceiver#onMessageReceived(java.lang.String, java.lang.String)
     */
    @Override
    public void onMessageReceived(final String route, final String message) {

        // log event
        LOG.debug("Received message with routing [{}]", route);

        // unpack received message data
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<TreeMap<Integer, Integer>> typeRef = new TypeReference<TreeMap<Integer, Integer>>() {
            // empty
        };
        try {

            // print message data
            TreeMap<Integer, Integer> statistics = mapper.readValue(message, typeRef);
            for (Integer month : statistics.keySet()) {
                String monthName = new DateFormatSymbols().getMonths()[month - 1];
                LOG.debug("Students born in {}: {}", monthName, statistics.get(month));
            }

        } catch (IOException e) {
            LOG.error(e);
            e.printStackTrace();
        }
    }

    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        bus.close();
    }
}
