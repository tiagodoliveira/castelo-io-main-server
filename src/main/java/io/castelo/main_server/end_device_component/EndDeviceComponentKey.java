package io.castelo.main_server.end_device_component;

import java.io.Serializable;
import java.util.Objects;

public class EndDeviceComponentKey implements Serializable {
    private String endDeviceMac;
    private Short componentNumber;

    public EndDeviceComponentKey() {
    }

    public EndDeviceComponentKey(String endDeviceMac, Short componentNumber) {
        this.endDeviceMac = endDeviceMac;
        this.componentNumber = componentNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EndDeviceComponentKey endDeviceComponentKey = (EndDeviceComponentKey) o;

        return Objects.equals(endDeviceMac, endDeviceComponentKey.endDeviceMac) && Objects.equals(componentNumber, endDeviceComponentKey.componentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endDeviceMac, componentNumber);
    }
}
