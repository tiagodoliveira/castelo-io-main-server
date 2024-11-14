package io.castelo.main_server.switch_data;

import java.time.LocalDateTime;

public record SwitchData(
        String endDeviceMac,
        int switchNumber,
        LocalDateTime timestamp,
        boolean value
) {
}
