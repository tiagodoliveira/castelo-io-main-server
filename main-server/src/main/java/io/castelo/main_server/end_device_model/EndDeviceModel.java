package io.castelo.main_server.end_device_model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "end_device_models")
public class EndDeviceModel{

    @Id
    @NotNull
    @Column(name = "model_id", updatable = false, nullable = false)
    private Integer modelId;

    @NotBlank
    @Column(name = "latest_firmware_version", nullable = false, columnDefinition = "text")
    private String latestFirmwareVersion;

    public EndDeviceModel(@NotNull Integer modelId, @NotBlank String latestFirmwareVersion) {
        this.modelId = modelId;
        this.latestFirmwareVersion = latestFirmwareVersion;
    }

    public EndDeviceModel() {

    }

    public Integer getModelId() {
        return modelId;
    }

    public String getLatestFirmwareVersion() {
        return latestFirmwareVersion;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public void setLatestFirmwareVersion(@NotBlank String latestFirmwareVersion) {
        this.latestFirmwareVersion = latestFirmwareVersion;
    }
}
