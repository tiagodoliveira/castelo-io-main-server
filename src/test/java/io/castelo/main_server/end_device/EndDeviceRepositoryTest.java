package io.castelo.main_server.end_device;

import io.castelo.main_server.end_device_model.EndDeviceModel;
import io.castelo.main_server.gateway.Gateway;
import io.castelo.main_server.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EndDeviceRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private EndDeviceRepository endDeviceRepository;

    @Autowired
    private EntityManager entityManager;

    private User testUser;
    private Gateway testGateway;
    private EndDeviceModel testModel;

    @BeforeEach
    void setUp() {
        testUser = new User(UUID.randomUUID(), "TestUser");
        entityManager.persist(testUser);

        testGateway = new Gateway("00:A0:C9:14:C8:00", testUser, "192.168.1.1", "TestGateway");
        entityManager.persist(testGateway);

        testModel = new EndDeviceModel(1, "v1.0");
        entityManager.persist(testModel);

        EndDevice endDevice = new EndDevice(
                "00:1A:2B:3C:4D:5E",
                "192.168.1.2",
                testModel,
                "TestDevice",
                false,
                testUser,
                testGateway,
                "v1.0",
                WorkingModes.AUTONOMOUS
        );

        EndDevice anotherEndDevice = new EndDevice(
                "00:1A:2B:3C:4D:6F",
                "192.168.1.4",
                testModel,
                "AnotherTestDevice",
                true,
                testUser,
                testGateway,
                "v1.3",
                WorkingModes.MANUAL
        );

        entityManager.persist(anotherEndDevice);
        entityManager.persist(endDevice);
    }

    @Test
    void connectionEstablished() {
        // Check if Testcontainers PostgreSQL is up and running
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldReturnEndDeviceByMac() {
        // Test finding EndDevice by ID
        EndDevice endDevice = endDeviceRepository.findById("00:1A:2B:3C:4D:5E").orElseThrow();
        assertThat(endDevice).isNotNull();
        assertThat(endDevice.getEndDeviceName()).isEqualTo("TestDevice");
    }

    @Test
    void shouldSaveEndDevice() {
        // Test saving a new EndDevice
        EndDevice newDevice = new EndDevice(
                "00:1A:2B:3C:4D:62",
                "192.168.1.3",
                testModel,
                "NewTestDevice",
                true,
                testUser,
                testGateway,
                "v1.2",
                WorkingModes.MANUAL
        );
        endDeviceRepository.save(newDevice);

        EndDevice savedDevice = endDeviceRepository.findById("00:1A:2B:3C:4D:62").orElseThrow();
        assertThat(savedDevice).isNotNull();
        assertThat(savedDevice.getEndDeviceName()).isEqualTo("NewTestDevice");
    }

    @Test
    void shouldFindAllEndDevices() {
        assertThat(endDeviceRepository.findAll().size()).isEqualTo(1);
    }
}