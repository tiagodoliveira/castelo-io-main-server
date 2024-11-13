package io.castelo.main_server.end_device_sensor_data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "${spring.data.mongodb.collections.sensor_data}")
public record SensorValueDBEntry(
        @Id String id,
        String endDeviceMac,
        int sensorNumber,
        LocalDateTime timestamp,
        String value
) {}