package io.castelo.main_server.end_device_sensor_data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "${spring.data.mongodb.collections.switch_data}")
public record SwitchValueDBEntry(
        @Id String id,
        String endDeviceMac,
        int switchNumber,
        LocalDateTime timestamp,
        boolean value
) {}