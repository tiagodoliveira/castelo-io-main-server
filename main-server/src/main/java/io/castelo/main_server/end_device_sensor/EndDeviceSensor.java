package io.castelo.main_server.end_device_sensor;

import io.castelo.main_server.utils.MACAddressValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

public record EndDeviceSensor(
        @Id
        String endDeviceMac,
        @NotEmpty
        int sensorNumber,
        @NotBlank
        String sensorName

) {
    public EndDeviceSensor {
        // Validate and normalize endDeviceMac
        if (endDeviceMac == null || endDeviceMac.isBlank()) {
            throw new IllegalArgumentException("End Device Mac must not be blank");
        }
        endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
    }
}
