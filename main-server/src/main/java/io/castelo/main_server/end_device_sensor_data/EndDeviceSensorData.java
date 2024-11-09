package io.castelo.main_server.end_device_sensor_data;

import io.castelo.main_server.utils.MACAddressValidator;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import java.util.List;

public record EndDeviceSensorData(
        @Id
        String endDeviceMac,
        @NotEmpty
        List<Sensor> sensors
) {
        public EndDeviceSensorData {
                // Validate and normalize endDeviceMac
                if (endDeviceMac == null || endDeviceMac.isBlank()) {
                        throw new IllegalArgumentException("End Device Mac must not be blank");
                }
                endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
        }
}
