package io.castelo.main_server.end_device_switch;

import io.castelo.main_server.utils.MACAddressValidator;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;

@Entity
@IdClass(SwitchKey.class)
@Table(name = "Switch")
public record Switch(
        @Id
        String endDeviceMac,
        @Id
        Short switchNumber,
        @NotBlank
        String switchName

) {
    public Switch {
        // Validate and normalize endDeviceMac
        if (endDeviceMac == null || endDeviceMac.isBlank()) {
            throw new IllegalArgumentException("End Device Mac must not be blank");
        }
        endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
    }
}
