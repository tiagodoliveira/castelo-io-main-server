package io.castelo.main_server.end_device_sensor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, SensorKey> {
}
