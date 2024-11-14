package io.castelo.main_server.sensor_data;

import java.time.LocalDateTime;

public record SensorValue(LocalDateTime timestamp, String value) {}
