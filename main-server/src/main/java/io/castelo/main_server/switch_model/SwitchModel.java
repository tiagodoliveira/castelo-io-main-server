package io.castelo.main_server.switch_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.castelo.main_server.end_device_model.EndDeviceModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@IdClass(SwitchModelKey.class)
@Table(name = "switch_models")
public class SwitchModel {

    @Id
    @Column(name = "model_id")
    private Integer modelId;

    @Id
    @Column(name = "switch_number")
    private Short switchNumber;

    @NotBlank
    @Column(name = "switch_name", nullable = false, columnDefinition = "text")
    private String switchName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", insertable = false, updatable = false)
    @JsonIgnore
    private EndDeviceModel endDeviceModel;

    public SwitchModel(@NotBlank Integer modelId, @NotNull Short switchNumber, @NotBlank String switchName){
        this.modelId = modelId;
        this.switchNumber = switchNumber;
        this.switchName = switchName;
    }

    public SwitchModel() {

    }

    public Short getSwitchNumber() {
        return switchNumber;
    }

    public void setSwitchNumber(Short switchNumber) {
        this.switchNumber = switchNumber;
    }

    public @NotBlank String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(@NotBlank String switchName) {
        this.switchName = switchName;
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
