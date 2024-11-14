package io.castelo.main_server.end_device_data.sensor_data;

import java.time.LocalDateTime;

public record SensorValue(LocalDateTime timestamp, String value) {}
