package io.castelo.main_server.end_device_data.switch_data;

import java.time.LocalDateTime;

public record SwitchValueRequest(
        String endDeviceMac,
        int switchNumber,
        LocalDateTime timestamp,
        boolean value
) {
}
