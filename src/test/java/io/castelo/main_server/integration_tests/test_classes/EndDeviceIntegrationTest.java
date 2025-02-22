package io.castelo.main_server.integration_tests.test_classes;

import io.castelo.main_server.end_device.EndDevice;
import io.castelo.main_server.end_device.WorkingModes;
import io.castelo.main_server.end_device_model.EndDeviceModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;


public class EndDeviceIntegrationTest extends BaseIntegrationTest {

    private static final String INVALID_END_DEVICE_MAC = "AA:BB:CC:DD:AA:AA";

    @Test
    public void testPasswordEncoding() {
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
                validUser1,
                validGateway1,
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
    void updateEndDevice() {
        validEndDevice1.setEndDeviceName("Updated Device Name");
        validEndDevice1.setEndDeviceIp("192.168.0.199");
        validEndDevice1.setDebugMode(true);
        validEndDevice1.setFirmware("1.3.0");
        validEndDevice1.setWorkingMode(WorkingModes.MANUAL);

        HttpEntity<EndDevice> request = new HttpEntity<>(validEndDevice1, headers);
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
    void shouldPairEndDeviceWithGateway() {
        String path = "/end-devices/" + VALID_END_DEVICE_MAC + "/pair-with-gateway/" + VALID_GATEWAY_MAC;
        HttpEntity<EndDevice> request = new HttpEntity<>(headers);

        ResponseEntity<EndDevice> response = restTemplate.exchange(path, HttpMethod.PUT, request, EndDevice.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(validEndDevice1.getGateway().getGatewayMac(), Objects.requireNonNull(response.getBody()).getGateway().getGatewayMac());
    }

    @Test
    void shouldReturnNotFoundIfInvalidUserOnCreatingEndDevice() {
        validEndDevice1.setOwner(invalidUser1);
        HttpEntity<EndDevice> request = new HttpEntity<>(validEndDevice1, headers);

        ResponseEntity<Void> endDeviceResponseEntity = restTemplate.exchange("/end-devices", HttpMethod.POST, request, Void.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, endDeviceResponseEntity.getStatusCode());
        validEndDevice1.setOwner(validUser1);
    }

    @Test
    void shouldReturnNotFoundIfInvalidGateway() {
        validEndDevice1.setGateway(invalidGateway1);
        HttpEntity<EndDevice> request = new HttpEntity<>(validEndDevice1, headers);

        ResponseEntity<EndDevice> endDeviceResponseEntity = restTemplate.withBasicAuth(USERNAME, PASSWORD)
                .exchange("/end-devices", HttpMethod.POST, request, EndDevice.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, endDeviceResponseEntity.getStatusCode());
        validEndDevice1.setGateway(validGateway1);
    }

    @Test
    void deleteEndDevice() {
        HttpEntity<EndDevice> request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange("/end-devices/" + VALID_END_DEVICE_MAC, HttpMethod.DELETE, request, Void.class);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        HttpEntity<Void> requestVerify = new HttpEntity<>(headers);
        ResponseEntity<EndDevice> getResponse = restTemplate.exchange("/end-devices/" + validEndDevice1.getEndDeviceMac(), HttpMethod.GET, requestVerify, EndDevice.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}