package io.castelo.main_server.sensor_model;

import java.io.Serializable;
import java.util.Objects;

public class SensorModelKey implements Serializable {
    private int modelId;
    private Short sensorNumber;

    public SensorModelKey() {
    }

    public SensorModelKey(int modelId, Short sensorNumber) {
        this.modelId = modelId;
        this.sensorNumber = sensorNumber;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public Short getSensorNumber() {
        return sensorNumber;
    }

    public void setSensorNumber(Short sensorNumber) {
        this.sensorNumber = sensorNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SensorModelKey sensorModelKey = (SensorModelKey) o;

        return Objects.equals(modelId, sensorModelKey.modelId) && Objects.equals(sensorNumber, sensorModelKey.sensorNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelId, sensorNumber);
    }
}
