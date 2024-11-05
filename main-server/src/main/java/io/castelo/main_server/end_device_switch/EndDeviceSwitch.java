package io.castelo.main_server.end_device_switch;

import io.castelo.main_server.utils.MACAddressValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

public record EndDeviceSwitch(
        @Id
        String endDeviceMac,
        @NotEmpty
        int switchNumber,
        @NotBlank
        String switchName

) {
    public EndDeviceSwitch {
        // Validate and normalize endDeviceMac
        if (endDeviceMac == null || endDeviceMac.isBlank()) {
            throw new IllegalArgumentException("End Device Mac must not be blank");
        }
        endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
    }
}
