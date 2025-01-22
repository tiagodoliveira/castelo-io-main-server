package io.castelo.main_server.end_device;

import io.castelo.main_server.end_device_model.EndDeviceModel;
import io.castelo.main_server.gateway.Gateway;
import io.castelo.main_server.user.User;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class EndDeviceControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    TestRestTemplate restTemplate;

    private static final String VALID_USER_ID = "cfd3ce69-1f3d-43cc-87f1-a686373a25ca";
    private static final String INVALID_USER_ID = "3af90dd5-f14f-4326-bdb9-b92db3175b36"; // Random UUID
    private static final String VALID_GATEWAY_MAC = "00:11:22:33:44:55";
    private static final String INVALID_GATEWAY_MAC = "00:00:00:00:00:01";
    private static final String VALID_END_DEVICE_MAC = "AA:BB:CC:DD:EE:FF";
    private static final String INVALID_END_DEVICE_MAC = "AA:BB:CC:DD:AA:AA";

    private HttpHeaders headers;
    private User invalidUser;
    private Gateway invalidGateway;
    private EndDevice newEndDevice;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        User validUser = new User(VALID_USER_ID, null, null, null, null, true, true);
        invalidUser = new User(INVALID_USER_ID, null, null, null, null, true, true);

        Gateway validGateway = new Gateway(VALID_GATEWAY_MAC, validUser, "1234", "Gateway");
        invalidGateway = new Gateway(INVALID_GATEWAY_MAC, validUser, "1234", "Gateway");

        newEndDevice = new EndDevice(
                "AA:BB:CC:DD:EE:FA",
                "192.168.0.1",
                new EndDeviceModel(1, "v1.2.3"),
                "New Device",
                false,
                validUser,
                validGateway,
                "1.0.0",
                WorkingModes.MANUAL
        );
    }

    @Test
    void getAllEndDevices() {
        ResponseEntity<EndDevice[]> response = restTemplate.getForEntity("/end-devices", EndDevice[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        EndDevice[] endDevices = response.getBody();
        assertNotNull(endDevices);
        assertEquals(2, endDevices.length);
    }

    @Test
    void whenGettingEndDeviceThatExistsShouldReturnEndDevice() {
        ResponseEntity<EndDevice> response = restTemplate.getForEntity("/end-devices/" + VALID_END_DEVICE_MAC, EndDevice.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        EndDevice endDevice = response.getBody();
        assertNotNull(endDevice);
        assertEquals("Device 1", endDevice.getEndDeviceName()); // Assuming data loaded from JSON
    }

    @Test
    void whenGettingEndDeviceThatDoesNotExistShouldReturnNotFound() {
        ResponseEntity<EndDevice> response = restTemplate.getForEntity("/end-devices/" + INVALID_END_DEVICE_MAC, EndDevice.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldCreateValidEndDevice() {
        HttpEntity<EndDevice> request = new HttpEntity<>(newEndDevice, headers);

        ResponseEntity<EndDevice> endDeviceResponseEntity = restTemplate.exchange("/end-devices", HttpMethod.POST, request, EndDevice.class);
        assertEquals(endDeviceResponseEntity.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(endDeviceResponseEntity.getBody());
        assertEquals(Objects.requireNonNull(endDeviceResponseEntity.getBody()).getEndDeviceName(), "New Device");
    }

    @Test
    void shouldReturnNotFoundIfInvalidUser() {
        newEndDevice.setUser(invalidUser);
        HttpEntity<EndDevice> request = new HttpEntity<>(newEndDevice, headers);

        ResponseEntity<EndDevice> endDeviceResponseEntity = restTemplate.exchange("/end-devices", HttpMethod.POST, request, EndDevice.class);
        assertEquals(endDeviceResponseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnNotFoundIfInvalidGateway() {
        newEndDevice.setGateway(invalidGateway);
        HttpEntity<EndDevice> request = new HttpEntity<>(newEndDevice, headers);

        ResponseEntity<EndDevice> endDeviceResponseEntity = restTemplate.exchange("/end-devices", HttpMethod.POST, request, EndDevice.class);
        assertEquals(endDeviceResponseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void updateEndDevice() {
        EndDeviceDTO updatedEndDeviceData = new EndDeviceDTO(
                "192.168.0.199",
                "Updated Device Name",
                true,
                new Gateway(VALID_GATEWAY_MAC, null, "192.168.1.1", "UpdatedGatewayName"),
                "1.2.3",
                WorkingModes.MANUAL
        );

        HttpEntity<EndDeviceDTO> request = new HttpEntity<>(updatedEndDeviceData, headers);

        ResponseEntity<EndDevice> response = restTemplate.exchange("/end-devices/" + VALID_END_DEVICE_MAC, HttpMethod.PUT, request, EndDevice.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getEndDeviceIp(), "192.168.0.199");
        assertEquals(response.getBody().getEndDeviceName(), "Updated Device Name");
        assertTrue(response.getBody().isDebugMode());
        assertEquals(response.getBody().getFirmware(), "1.2.3");
        assertEquals(response.getBody().getWorkingMode(), WorkingModes.MANUAL);
    }

    @Test
    void deleteEndDevice() {
        ResponseEntity<Void> response = restTemplate.exchange("/end-devices/" + VALID_END_DEVICE_MAC, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<EndDevice> getResponse = restTemplate.getForEntity("/end-devices/" + VALID_END_DEVICE_MAC, EndDevice.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}