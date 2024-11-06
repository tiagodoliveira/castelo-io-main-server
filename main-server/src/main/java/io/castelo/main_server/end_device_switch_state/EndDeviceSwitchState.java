package io.castelo.main_server.end_device_switch_state;

import io.castelo.main_server.utils.MACAddressValidator;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record EndDeviceSwitchState(
        @Id
        String endDeviceMac,
        @NotEmpty
        int switchNumber,
        @DateTimeFormat @NotEmpty
        LocalDateTime timestamp,
        @NotEmpty
        boolean switchValue
) {
        public EndDeviceSwitchState {
                // Validate and normalize endDeviceMac
                if (endDeviceMac == null || endDeviceMac.isBlank()) {
                        throw new IllegalArgumentException("End Device Mac must not be blank");
                }
                endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);

                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        }
}
