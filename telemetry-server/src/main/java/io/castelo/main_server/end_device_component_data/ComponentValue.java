package io.castelo.main_server.end_device_component_data;

import java.time.LocalDateTime;

public record ComponentValue(LocalDateTime timestamp, String value) {}
