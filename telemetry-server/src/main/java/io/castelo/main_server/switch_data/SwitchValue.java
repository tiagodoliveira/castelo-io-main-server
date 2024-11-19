package io.castelo.main_server.switch_data;

import java.time.LocalDateTime;

public record SwitchValue(LocalDateTime timestamp, boolean value) {}
