package io.castelo.main_server.end_device_model;

import io.castelo.main_server.sensor_model.SensorModel;
import io.castelo.main_server.switch_model.SwitchModel;
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
    private List<SensorModel> sensorModels;

    @OneToMany(mappedBy = "endDeviceModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SwitchModel> switchModels;

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

    public List<SensorModel> getSensorModels() {
        return sensorModels;
    }

    public void setSensorModels(List<SensorModel> sensorModels) {
        this.sensorModels = sensorModels;
    }

    public List<SwitchModel> getSwitchModels() {
        return switchModels;
    }

    public void setSwitchModels(List<SwitchModel> switchModels) {
        this.switchModels = switchModels;
    }
}
