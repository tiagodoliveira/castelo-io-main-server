package io.castelo.main_server.end_device_sensor;

import io.castelo.main_server.utils.MACAddressValidator;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;

@Entity
@IdClass(SensorKey.class)
@Table(name = "Sensor")
public record Sensor(
        @Id
        String endDeviceMac,
        @Id
        Short sensorNumber,
        @NotBlank
        String sensorName

) {
    public Sensor {
        // Validate and normalize endDeviceMac
        if (endDeviceMac == null || endDeviceMac.isBlank()) {
            throw new IllegalArgumentException("End Device Mac must not be blank");
        }
        endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
    }
}
