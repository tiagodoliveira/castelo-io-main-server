package io.castelo.main_server.end_device_sensor_data;

import java.time.LocalDateTime;

public record SensorValueRequest(
        String endDeviceMac,
        int sensorNumber,
        LocalDateTime timestamp,
        String value
) {
}
