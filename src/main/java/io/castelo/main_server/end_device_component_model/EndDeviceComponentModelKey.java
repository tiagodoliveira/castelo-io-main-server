package io.castelo.main_server.end_device_component_model;

import java.io.Serializable;
import java.util.Objects;

public class EndDeviceComponentModelKey implements Serializable {
    private Integer modelId;
    private Short componentNumber;

    public EndDeviceComponentModelKey() {
    }

    public EndDeviceComponentModelKey(Integer modelId, Short componentNumber) {
        this.modelId = modelId;
        this.componentNumber = componentNumber;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Short getComponentNumber() {
        return componentNumber;
    }

    public void setComponentNumber(Short componentNumber) {
        this.componentNumber = componentNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EndDeviceComponentModelKey endDeviceComponentModelKey = (EndDeviceComponentModelKey) o;

        return Objects.equals(modelId, endDeviceComponentModelKey.modelId) && Objects.equals(componentNumber, endDeviceComponentModelKey.componentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelId, componentNumber);
    }
}
