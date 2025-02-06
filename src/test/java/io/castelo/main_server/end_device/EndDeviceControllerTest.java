package io.castelo.main_server.end_device;

import io.castelo.main_server.end_device_model.EndDeviceModel;
import io.castelo.main_server.gateway.Gateway;
import io.castelo.main_server.user.User;
import io.castelo.main_server.user.UserRoles;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class EndDeviceControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EntityManager entityManager;

    private static final String VALID_USER_ID = "cfd3ce69-1f3d-43cc-87f1-a686373a25ca";
    private static final String INVALID_USER_ID = "3af90dd5-f14f-4326-bdb9-b92db3175b36"; // Random UUID
    private static final String VALID_GATEWAY_MAC = "00:11:22:33:44:55";
    private static final String INVALID_GATEWAY_MAC = "00:00:00:00:00:01";
    private static final String VALID_END_DEVICE_MAC = "AA:BB:CC:DD:EE:FF";
    private static final String INVALID_END_DEVICE_MAC = "AA:BB:CC:DD:AA:AA";
    private static final String USERNAME = "alice@gmail.com";
    private static final String PASSWORD = "password2";
    private static String encodedPassword; // Encoded password for "password2"

    private HttpHeaders headers;
    private User invalidUser;
    private User validUser;
    private Gateway invalidGateway;
    private Gateway validGateway;
    private EndDevice newEndDevice;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        encodedPassword = passwordEncoder.encode(PASSWORD);

        validUser = new User(UUID.fromString(VALID_USER_ID), USERNAME, encodedPassword, "Test User", UserRoles.USER, true, true);
        entityManager.persist(validUser);

        invalidUser = new User(UUID.fromString(INVALID_USER_ID), "invalidUser", "invalidPassword", "invalidUser", UserRoles.USER, true, true);

        validGateway = new Gateway(VALID_GATEWAY_MAC, validUser, "1234", "Gateway");
        entityManager.persist(validGateway);
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

        entityManager.persist(newEndDevice);
    }

    @Test
    void testPasswordEncoding() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        assertTrue(encoder.matches("password2", encodedPassword));
    }

    @Test
    void getAllEndDevices() {
        ResponseEntity<EndDevice[]> response = restTemplate.withBasicAuth(USERNAME, PASSWORD).getForEntity("/end-devices", EndDevice[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        EndDevice[] endDevices = response.getBody();
        assertNotNull(endDevices);
        assertEquals(2, endDevices.length);
    }

    @Test
    void whenGettingEndDeviceThatExistsShouldReturnEndDevice() {
        ResponseEntity<EndDevice> response = restTemplate.withBasicAuth(USERNAME, PASSWORD).getForEntity("/end-devices/" + VALID_END_DEVICE_MAC, EndDevice.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        EndDevice endDevice = response.getBody();
        assertNotNull(endDevice);
        assertEquals("Device 1", endDevice.getEndDeviceName()); // Assuming data loaded from JSON
    }

    @Test
    void whenGettingEndDeviceThatDoesNotExistShouldReturnNotFound() {
        ResponseEntity<EndDevice> response = restTemplate.withBasicAuth(USERNAME, PASSWORD).getForEntity("/end-devices/" + INVALID_END_DEVICE_MAC, EndDevice.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldCreateValidEndDevice() {
        HttpEntity<EndDevice> request = new HttpEntity<>(newEndDevice, headers);

        ResponseEntity<EndDevice> endDeviceResponseEntity = restTemplate.withBasicAuth(USERNAME, PASSWORD)
                .exchange("/end-devices", HttpMethod.POST, request, EndDevice.class);
        assertEquals(HttpStatus.CREATED, endDeviceResponseEntity.getStatusCode());
        assertNotNull(endDeviceResponseEntity.getBody());
        assertEquals(Objects.requireNonNull(endDeviceResponseEntity.getBody()).getEndDeviceName(), "New Device");
    }

    @Test
    void shouldReturnNotFoundIfInvalidUserOnCreatingEndDevice() {
        newEndDevice.setUser(invalidUser);
        HttpEntity<EndDevice> request = new HttpEntity<>(newEndDevice, headers);

        ResponseEntity<EndDevice> endDeviceResponseEntity = restTemplate.withBasicAuth(USERNAME, PASSWORD)
                .exchange("/end-devices", HttpMethod.POST, request, EndDevice.class);
        assertEquals(HttpStatus.NOT_FOUND, endDeviceResponseEntity.getStatusCode());
    }

    @Test
    void shouldReturnNotFoundIfInvalidGateway() {
        newEndDevice.setGateway(invalidGateway);
        HttpEntity<EndDevice> request = new HttpEntity<>(newEndDevice, headers);

        ResponseEntity<EndDevice> endDeviceResponseEntity = restTemplate.withBasicAuth(USERNAME, PASSWORD)
                .exchange("/end-devices", HttpMethod.POST, request, EndDevice.class);
        assertEquals(HttpStatus.NOT_FOUND, endDeviceResponseEntity.getStatusCode());
    }

    @Test
    void updateEndDevice() {
        EndDeviceDTO updatedEndDeviceData = new EndDeviceDTO(
                "192.168.0.199",
                "Updated Device Name",
                true,
                new Gateway(VALID_GATEWAY_MAC, validUser, "192.168.1.1", "UpdatedGatewayName"),
                "1.2.3",
                WorkingModes.MANUAL
        );

        HttpEntity<EndDeviceDTO> request = new HttpEntity<>(updatedEndDeviceData, headers);

        ResponseEntity<EndDevice> response = restTemplate.withBasicAuth(USERNAME, PASSWORD).exchange("/end-devices/" + VALID_END_DEVICE_MAC, HttpMethod.PUT, request, EndDevice.class);

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
        ResponseEntity<Void> response = restTemplate.withBasicAuth(USERNAME, PASSWORD).exchange("/end-devices/" + VALID_END_DEVICE_MAC, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<EndDevice> getResponse = restTemplate.withBasicAuth(USERNAME, PASSWORD).getForEntity("/end-devices/" + VALID_END_DEVICE_MAC, EndDevice.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}