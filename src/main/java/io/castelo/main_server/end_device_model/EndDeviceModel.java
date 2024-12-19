package io.castelo.main_server.end_device_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.castelo.main_server.end_device_component_model.EndDeviceComponentModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

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

    @OneToMany(mappedBy = "endDeviceModel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<EndDeviceComponentModel> endDeviceComponentModels;

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

    public List<EndDeviceComponentModel> getEndDeviceComponentModels() {
        return endDeviceComponentModels;
    }

    public void setEndDeviceComponentModels(List<EndDeviceComponentModel> endDeviceComponentModels) {
        this.endDeviceComponentModels = endDeviceComponentModels;
    }

    @Override
    public String toString() {
        return "EndDeviceModel{" +
                "modelId=" + modelId +
                ", latestFirmwareVersion='" + latestFirmwareVersion + '\'' +
                ", endDeviceComponentModels=" + endDeviceComponentModels +
                '}';
    }
}
