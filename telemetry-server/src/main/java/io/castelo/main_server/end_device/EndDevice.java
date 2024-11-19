package io.castelo.main_server.end_device;

import io.castelo.main_server.end_device_model.EndDeviceModel;
import io.castelo.main_server.gateway.Gateway;
import io.castelo.main_server.utils.MACAddressValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "EndDevice")
public record EndDevice(
        @Id
        String endDeviceMac,
        @NotBlank
        String endDeviceIp,

        @ManyToOne
        @JoinColumn(name = "model_id", nullable = false)
        EndDeviceModel endDeviceModel,

        @NotBlank
        String endDeviceName,
        @NotEmpty
        boolean debugMode,

        @ManyToOne
        @JoinColumn(name = "gateway_mac", nullable = false)
        Gateway gateway,

        @NotBlank
        String firmware,

        @NotEmpty
        @Enumerated(EnumType.STRING)
        DeviceMode deviceMode
) {
        public EndDevice {
                // Validate and normalize endDeviceMac
                if (endDeviceMac == null || endDeviceMac.isBlank()) {
                        throw new IllegalArgumentException("End Device Mac must not be blank");
                }
                endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
        }
}
