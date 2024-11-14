package io.castelo.main_server.sensor_data;

import java.time.LocalDateTime;

public record SensorData(
        String endDeviceMac,
        int sensorNumber,
        LocalDateTime timestamp,
        String value
) {
}
