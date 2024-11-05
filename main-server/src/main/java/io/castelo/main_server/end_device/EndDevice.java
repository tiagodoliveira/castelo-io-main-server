package io.castelo.main_server.end_device;

import io.castelo.main_server.utils.MACAddressValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

public record EndDevice(
        @Id
        String endDeviceMac,
        @NotBlank
        String endDeviceIp,
        @NotEmpty
        int modelId,
        @NotBlank
        String endDeviceName,
        @NotEmpty
        boolean debugMode,
        @NotBlank
        String gatewayMac,
        @NotBlank
        String firmware,
        @NotEmpty
        WorkingMode workingMode
) {
        public EndDevice {
                // Validate and normalize endDeviceMac
                if (endDeviceMac == null || endDeviceMac.isBlank()) {
                        throw new IllegalArgumentException("End Device Mac must not be blank");
                }
                endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);

                // Validate and normalize gatewayMac
                if (gatewayMac == null || gatewayMac.isBlank()) {
                        throw new IllegalArgumentException("Gateway Mac must not be blank");
                }
                gatewayMac = MACAddressValidator.normalizeMACAddress(gatewayMac);
        }
}
