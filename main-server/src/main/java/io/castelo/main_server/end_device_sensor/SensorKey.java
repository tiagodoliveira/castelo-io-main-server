package io.castelo.main_server.end_device_sensor;

import java.io.Serializable;
import java.util.Objects;

public class SensorKey implements Serializable {
    private String endDeviceMac;
    private Short sensorNumber;

    public SensorKey() {
    }

    public SensorKey(String endDeviceMac, Short sensorNumber) {
        this.endDeviceMac = endDeviceMac;
        this.sensorNumber = sensorNumber;
    }

    public String getEndDeviceMac() {
        return endDeviceMac;
    }

    public void setEndDeviceMac(String endDeviceMac) {
        this.endDeviceMac = endDeviceMac;
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

        SensorKey sensorKey = (SensorKey) o;

        return Objects.equals(endDeviceMac, sensorKey.endDeviceMac) && Objects.equals(sensorNumber, sensorKey.sensorNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endDeviceMac, sensorNumber);
    }
}
