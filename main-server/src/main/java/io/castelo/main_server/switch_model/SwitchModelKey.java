package io.castelo.main_server.switch_model;

import java.io.Serializable;
import java.util.Objects;

public class SwitchModelKey implements Serializable {
    private int modelId;
    private Short switchNumber;

    public SwitchModelKey() {
    }

    public SwitchModelKey(int modelId, Short switchNumber) {
        this.modelId = modelId;
        this.switchNumber = switchNumber;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public Short getSwitchNumber() {
        return switchNumber;
    }

    public void setSwitchNumber(Short switchNumber) {
        this.switchNumber = switchNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SwitchModelKey switchModelKey = (SwitchModelKey) o;

        return Objects.equals(modelId, switchModelKey.modelId) && Objects.equals(switchNumber, switchModelKey.switchNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelId, switchNumber);
    }
}
