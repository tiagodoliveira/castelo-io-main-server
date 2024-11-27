package io.castelo.main_server.end_device_component_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.castelo.main_server.end_device_component.ComponentTypes;
import io.castelo.main_server.end_device_model.EndDeviceModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@IdClass(EndDeviceComponentModelKey.class)
@Table(name = "end_device_component_models")
public class EndDeviceComponentModel {

    @Id
    @Column(name = "model_id")
    @JsonIgnore
    private Integer modelId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "component_type", nullable = false, columnDefinition = "component_types")
    private ComponentTypes componentType;

    @Id
    @Column(name = "component_number")
    private Short componentNumber;

    @NotBlank
    @Column(name = "component_name", nullable = false, columnDefinition = "text")
    private String componentName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", insertable = false, updatable = false)
    @JsonIgnore
    private EndDeviceModel endDeviceModel;


    public EndDeviceComponentModel(@NotNull Integer modelId,  @NotNull ComponentTypes componentType, @NotNull Short componentNumber, @NotBlank String componentName){
        this.modelId = modelId;
        this.componentType = componentType;
        this.componentNumber = componentNumber;
        this.componentName = componentName;
    }

    public EndDeviceComponentModel() {

    }

    public Short getComponentNumber() {
        return componentNumber;
    }

    public void setComponentNumber(Short componentNumber) {
        this.componentNumber = componentNumber;
    }

    public @NotBlank String getComponentName() {
        return componentName;
    }

    public void setComponentName(@NotBlank String componentName) {
        this.componentName = componentName;
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

    public @NotNull ComponentTypes getComponentType() {
        return componentType;
    }

    public void setComponentType(@NotNull ComponentTypes componentType) {
        this.componentType = componentType;
    }
}
