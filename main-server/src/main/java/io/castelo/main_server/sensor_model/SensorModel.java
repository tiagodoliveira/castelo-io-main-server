package io.castelo.main_server.sensor_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.castelo.main_server.end_device_model.EndDeviceModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@IdClass(SensorModelKey.class)
@Table(name = "sensor_models")
public class SensorModel {

    @Id
    @Column(name = "model_id")
    private Integer modelId;

    @Id
    @Column(name = "sensor_number")
    private Short sensorNumber;

    @NotBlank
    @Column(name = "sensor_name", nullable = false, columnDefinition = "text")
    private String sensorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", insertable = false, updatable = false)
    @JsonIgnore
    private EndDeviceModel endDeviceModel;


    public SensorModel(@NotNull Integer modelId, @NotNull Short sensorNumber, @NotBlank String sensorName){
        this.modelId = modelId;
        this.sensorNumber = sensorNumber;
        this.sensorName = sensorName;
    }

    public SensorModel() {

    }

    public Short getSensorNumber() {
        return sensorNumber;
    }

    public void setSensorNumber(Short sensorNumber) {
        this.sensorNumber = sensorNumber;
    }

    public @NotBlank String getSensorName() {
        return sensorName;
    }

    public void setSensorName(@NotBlank String sensorName) {
        this.sensorName = sensorName;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public EndDeviceModel getEndDeviceModel() {
        return endDeviceModel;
    }

    public void setEndDeviceModel(EndDeviceModel endDeviceModel) {
        this.endDeviceModel = endDeviceModel;
    }
}
