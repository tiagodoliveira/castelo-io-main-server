package io.castelo.main_server.end_device;

import io.castelo.main_server.end_device_model.EndDeviceModel;
import io.castelo.main_server.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class EndDeviceControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void getAllEndDevices() {
        EndDevice[] endDevices = restTemplate.getForObject("/end-devices", EndDevice[].class);
        assert endDevices != null;
        assert endDevices.length == 2;
    }

    @Test
    void getEndDevice() {
        EndDevice endDevice = restTemplate.getForObject("/end-devices/AA:BB:CC:DD:EE:FF", EndDevice.class);
        assert endDevice != null;
        assert endDevice.getEndDeviceName().equals("Device 1");
    }

    @Test
    void createEndDevice() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Specify content type as JSON

        EndDevice newEndDevice = new EndDevice(
                "AA:BB:CC:DD:EE:FA",
                "192.168.0.1",
                new EndDeviceModel(1, "v1.2.3"),
                "New Device",
                false,
                new User("cfd3ce69-1f3d-43cc-87f1-a686373a25ca", "Alice Smith"),
                null,
                "1.0.0",
                WorkingModes.MANUAL
        );
        HttpEntity<EndDevice> request = new HttpEntity<>(newEndDevice, headers);

        ResponseEntity<EndDevice> endDeviceResponseEntity = restTemplate.exchange("/end-devices", HttpMethod.POST, request, EndDevice.class);
        assertEquals(endDeviceResponseEntity.getStatusCode(), HttpStatus.CREATED);
        assertEquals(Objects.requireNonNull(endDeviceResponseEntity.getBody()).getEndDeviceName(), "New Device");
    }

    @Test
    void updateEndDevice() {
        //TODO
    }

    @Test
    void deleteEndDevice() {
        //TODO
    }
}