package io.castelo.main_server.end_device_component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.castelo.main_server.end_device.EndDevice;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@IdClass(EndDeviceComponentKey.class)
@Table(name = "end_device_components")
public class EndDeviceComponent {

    @Id
    @NotBlank
    @Column(name = "end_device_mac", length = 17)
    @JsonIgnore
    private String endDeviceMac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_device_mac", insertable = false, updatable = false)
    private EndDevice endDevice;

    @Id
    @Column(name = "component_number")
    private Short componentNumber;

    @NotBlank
    @Column(name = "component_name", nullable = false, columnDefinition = "text")
    private String componentName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "component_type", nullable = false, columnDefinition = "component_types")
    private ComponentTypes componentType;


    public EndDeviceComponent(@NotBlank String endDeviceMac, @NotNull Short componentNumber, @NotBlank String componentName, @NotNull ComponentTypes componentType){
        this.endDeviceMac = endDeviceMac;
        this.componentNumber = componentNumber;
        this.componentName = componentName;
        this.componentType = componentType;
    }

    public EndDeviceComponent() {

    }

    public String getEndDeviceMac() {
        return endDeviceMac;
    }

    public void setEndDeviceMac(String endDeviceMac) {
        this.endDeviceMac = endDeviceMac;
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

    public @NotNull ComponentTypes getComponentType() {
        return componentType;
    }

    public void setComponentType(@NotNull ComponentTypes componentType) {
        this.componentType = componentType;
    }

    @Override
    public String toString() {
        return "EndDeviceComponent{" +
                "endDeviceMac='" + endDeviceMac + '\'' +
                ", endDevice=" + endDevice +
                ", componentNumber=" + componentNumber +
                ", componentName='" + componentName + '\'' +
                ", componentType=" + componentType +
                '}';
    }
}
