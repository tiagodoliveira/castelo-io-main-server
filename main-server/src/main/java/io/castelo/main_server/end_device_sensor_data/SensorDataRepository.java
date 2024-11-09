package io.castelo.main_server.end_device_sensor_data;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SensorDataRepository {

    EndDeviceSensorData getSensorDataBySensorNumber(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime stop,
            String macAddress,
            int sensorNumber);

    void addSensorData(EndDeviceSensorData endDeviceSensorData);

    EndDeviceSensorData getAllSensorValues(String macAddress);
}
