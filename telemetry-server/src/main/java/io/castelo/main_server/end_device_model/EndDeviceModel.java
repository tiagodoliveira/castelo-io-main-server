package io.castelo.main_server.end_device_model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "EndDeviceModel")
public record EndDeviceModel(
        @Id Integer modelId,
        String latestFirmwareVersion
) {
}
