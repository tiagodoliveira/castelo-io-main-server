package io.castelo.main_server.end_device_sensor_state;

import io.castelo.main_server.utils.MACAddressValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record EndDeviceSensorState(
        @Id
        String endDeviceMac,
        @NotEmpty
        int sensorNumber,
        @DateTimeFormat @NotEmpty
        LocalDateTime timestamp,
        @NotBlank
        String sensorState
) {
        public EndDeviceSensorState {
                // Validate and normalize endDeviceMac
                if (endDeviceMac == null || endDeviceMac.isBlank()) {
                        throw new IllegalArgumentException("End Device Mac must not be blank");
                }
                endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
        }
}
