package io.castelo.main_server.end_device_switch;

import java.io.Serializable;
import java.util.Objects;

public class SwitchKey implements Serializable {
    private String endDeviceMac;
    private Short switchNumber;

    public SwitchKey() {
    }

    public SwitchKey(String endDeviceMac, Short switchNumber) {
        this.endDeviceMac = endDeviceMac;
        this.switchNumber = switchNumber;
    }

    public String getEndDeviceMac() {
        return endDeviceMac;
    }

    public void setEndDeviceMac(String endDeviceMac) {
        this.endDeviceMac = endDeviceMac;
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

        SwitchKey switchKey = (SwitchKey) o;

        return Objects.equals(endDeviceMac, switchKey.endDeviceMac) && Objects.equals(switchNumber, switchKey.switchNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endDeviceMac, switchNumber);
    }
}