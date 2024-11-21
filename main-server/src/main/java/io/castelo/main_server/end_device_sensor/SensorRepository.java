package io.castelo.main_server.end_device_sensor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, SensorKey> {
    List<Sensor> findAllByEndDeviceMac(String endDeviceMac);

}
