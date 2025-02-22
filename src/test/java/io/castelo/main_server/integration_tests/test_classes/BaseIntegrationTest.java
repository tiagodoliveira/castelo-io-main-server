package io.castelo.main_server.integration_tests.test_classes;

import io.castelo.main_server.auth.PasswordEncoder;
import io.castelo.main_server.auth.jwt.AuthTokenResponse;
import io.castelo.main_server.end_device.EndDevice;
import io.castelo.main_server.end_device.WorkingModes;
import io.castelo.main_server.end_device_model.EndDeviceModel;
import io.castelo.main_server.gateway.Gateway;
import io.castelo.main_server.integration_tests.TestContainerConfig;
import io.castelo.main_server.user.User;
import io.castelo.main_server.user.UserRoles;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Objects;
import java.util.UUID;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
public abstract class BaseIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = TestContainerConfig.getInstance();

    @Autowired
    protected TestRestTemplate restTemplate;

    protected static final String VALID_USER_ID = "cfd3ce69-1f3d-43cc-87f1-a686373a25ca";
    protected static final String VALID_GATEWAY_MAC = "00:11:22:33:44:55";
    private static final String INVALID_GATEWAY_MAC = "AA:BB:CC:DD:AA:00";
    protected static final String VALID_END_DEVICE_MAC = "AA:BB:CC:DD:EE:FF";
    protected static final String USERNAME = "alice@gmail.com";
    protected static final String PASSWORD = "password2";
    protected static String encodedPassword; // Encoded password for "password2"

    protected static HttpHeaders headers;
    protected static User invalidUser1;
    protected static User validUser1;
    protected static Gateway invalidGateway1;
    protected static Gateway validGateway1;
    protected static EndDevice validEndDevice1;

    @BeforeAll
    void globalSetup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        encodedPassword = PasswordEncoder.encodePassword(PASSWORD);

        validUser1 = new User(UUID.fromString(VALID_USER_ID), USERNAME, encodedPassword, "Test User", UserRoles.ADMIN, true, true);
        invalidUser1 = new User(UUID.randomUUID(), USERNAME, encodedPassword, "Test User", UserRoles.ADMIN, true, true);
        validGateway1 = new Gateway(VALID_GATEWAY_MAC, validUser1, "192.168.0.1", "Gateway");
        invalidGateway1 = new Gateway(INVALID_GATEWAY_MAC, validUser1, "192.168.0.1", "Gateway");
        validEndDevice1 = new EndDevice(
                VALID_END_DEVICE_MAC,
                "192.168.0.1",
                new EndDeviceModel(1, "v1.2.3"),
                "New Device",
                false,
                validUser1,
                validGateway1,
                "1.0.0",
                WorkingModes.AUTONOMOUS
        );

        ResponseEntity<AuthTokenResponse> response = restTemplate.withBasicAuth(USERNAME, PASSWORD).postForEntity("/login", null, AuthTokenResponse.class);
        String accessToken = Objects.requireNonNull(response.getBody()).accessToken();
        headers.setBearerAuth(accessToken);
    }
}