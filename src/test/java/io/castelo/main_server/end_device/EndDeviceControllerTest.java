package io.castelo.main_server.end_device;

import io.castelo.main_server.auth.jwt.AuthTokenResponse;
import io.castelo.main_server.end_device_model.EndDeviceModel;
import io.castelo.main_server.gateway.Gateway;
import io.castelo.main_server.user.User;
import io.castelo.main_server.user.UserRoles;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;
import java.util.UUID;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
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

    private static HttpHeaders headers;
    private static User invalidUser;
    private static User validUser;
    private static Gateway invalidGateway;
    private static Gateway validGateway;
    private static EndDevice newEndDevice;


    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        encodedPassword = passwordEncoder.encode(PASSWORD);

        validUser = new User(UUID.fromString(VALID_USER_ID), USERNAME, encodedPassword, "Test User", UserRoles.ADMIN, true, true);
        entityManager.persist(validUser);

        invalidUser = new User(UUID.fromString(INVALID_USER_ID), "invalidUser", "invalidPassword", "invalidUser", UserRoles.USER, true, true);

        validGateway = new Gateway(VALID_GATEWAY_MAC, validUser, "1234", "Gateway");
        entityManager.persist(validGateway);

        invalidGateway = new Gateway(INVALID_GATEWAY_MAC, validUser, "1234", "Gateway");

        newEndDevice = new EndDevice(
                VALID_END_DEVICE_MAC,
                "192.168.0.1",
                new EndDeviceModel(1, "v1.2.3"),
                "New Device",
                false,
                validUser,
                validGateway,
                "1.0.0",
                WorkingModes.AUTONOMOUS
        );

        entityManager.persist(newEndDevice);

        ResponseEntity<AuthTokenResponse> response = restTemplate.withBasicAuth(USERNAME, PASSWORD).postForEntity("/login", null, AuthTokenResponse.class);
        String accessToken = Objects.requireNonNull(response.getBody()).accessToken();
        headers.setBearerAuth(accessToken);
    }

    @Test
    void testPasswordEncoding() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        Assertions.assertTrue(encoder.matches(PASSWORD, encodedPassword));
    }

    @Test
    void getAllEndDevices() {
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<EndDevice[]> response = restTemplate.exchange("/end-devices", HttpMethod.GET, request, EndDevice[].class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(2, response.getBody().length);
    }

    @Test
    void whenGettingEndDeviceThatExistsShouldReturnEndDevice() {
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<EndDevice> response = restTemplate.exchange("/end-devices/" + VALID_END_DEVICE_MAC, HttpMethod.GET, request, EndDevice.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        EndDevice endDevice = response.getBody();
        Assertions.assertNotNull(endDevice);
        Assertions.assertEquals("Device 1", endDevice.getEndDeviceName());
    }

    @Test
    void whenGettingEndDeviceThatDoesNotExistShouldReturnNotFound() {
        ResponseEntity<EndDevice> response = restTemplate.withBasicAuth(USERNAME, PASSWORD).getForEntity("/end-devices/" + INVALID_END_DEVICE_MAC, EndDevice.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldCreateValidEndDevice() {
        EndDevice endDevice = new EndDevice(
                "AA:BB:CC:DD:EE:F1",
                "192.168.0.1",
                new EndDeviceModel(1, "v1.2.3"),
                "New Device",
                false,
                validUser,
                validGateway,
                "1.0.0",
                WorkingModes.MANUAL
        );
        HttpEntity<EndDevice> request = new HttpEntity<>(endDevice, headers);

        ResponseEntity<EndDevice> endDeviceResponseEntity = restTemplate.exchange("/end-devices", HttpMethod.POST, request, EndDevice.class);
        Assertions.assertEquals(HttpStatus.CREATED, endDeviceResponseEntity.getStatusCode());
        Assertions.assertNotNull(endDeviceResponseEntity.getBody());
        Assertions.assertEquals("New Device", Objects.requireNonNull(endDeviceResponseEntity.getBody()).getEndDeviceName());
    }

    @Test
    void shouldReturnNotFoundIfInvalidUserOnCreatingEndDevice() {
        newEndDevice.setOwner(invalidUser);
        HttpEntity<EndDevice> request = new HttpEntity<>(newEndDevice, headers);

        ResponseEntity<Void> endDeviceResponseEntity = restTemplate.exchange("/end-devices", HttpMethod.POST, request, Void.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, endDeviceResponseEntity.getStatusCode());
    }

    @Test
    void shouldReturnNotFoundIfInvalidGateway() {
        newEndDevice.setGateway(invalidGateway);
        HttpEntity<EndDevice> request = new HttpEntity<>(newEndDevice, headers);

        ResponseEntity<EndDevice> endDeviceResponseEntity = restTemplate.withBasicAuth(USERNAME, PASSWORD)
                .exchange("/end-devices", HttpMethod.POST, request, EndDevice.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, endDeviceResponseEntity.getStatusCode());
    }

    @Test
    void updateEndDevice() {
        newEndDevice.setEndDeviceName("Updated Device Name");
        newEndDevice.setEndDeviceIp("192.168.0.199");
        newEndDevice.setDebugMode(true);
        newEndDevice.setFirmware("1.3.0");
        newEndDevice.setWorkingMode(WorkingModes.MANUAL);

        HttpEntity<EndDevice> request = new HttpEntity<>(newEndDevice, headers);
        ResponseEntity<EndDevice> response = restTemplate.exchange("/end-devices", HttpMethod.PUT, request, EndDevice.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("192.168.0.199", response.getBody().getEndDeviceIp());
        Assertions.assertEquals("Updated Device Name", response.getBody().getEndDeviceName());
        Assertions.assertTrue(response.getBody().isDebugMode());
        Assertions.assertEquals("1.3.0", response.getBody().getFirmware());
        Assertions.assertEquals(WorkingModes.MANUAL, response.getBody().getWorkingMode());
    }

    @Test
    void deleteEndDevice() {
        HttpEntity<EndDevice> request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange("/end-devices/" + VALID_END_DEVICE_MAC, HttpMethod.DELETE, request, Void.class);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        HttpEntity<Void> requestVerify = new HttpEntity<>(headers);
        ResponseEntity<EndDevice> getResponse = restTemplate.exchange("/end-devices/" + newEndDevice.getEndDeviceMac(), HttpMethod.GET, requestVerify, EndDevice.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void shouldPairEndDeviceWithGateway() {
        String path = "/end-devices/" + VALID_END_DEVICE_MAC + "/pair-with-gateway/" + VALID_GATEWAY_MAC;
        HttpEntity<EndDevice> request = new HttpEntity<>(headers);

        ResponseEntity<EndDevice> response = restTemplate.exchange(path, HttpMethod.PUT, request, EndDevice.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(newEndDevice.getGateway().getGatewayMac(), Objects.requireNonNull(response.getBody()).getGateway().getGatewayMac());
    }
}