package io.castelo.main_server.end_device_switch;

import io.castelo.main_server.end_device.EndDevice;
import io.castelo.main_server.utils.MACAddressValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@IdClass(SwitchKey.class)
@Table(name = "switches")
public class Switch{

    @Id
    @Column(name = "end_device_mac", length = 17)
    String endDeviceMac;

    @Id
    @Column(name = "switch_number")
    Short switchNumber;

    @NotBlank
    @Column(name = "switch_name", nullable = false, columnDefinition = "text")
    String switchName;

    @ManyToOne
    @JoinColumn(name = "end_device_mac", insertable = false, updatable = false)
    private EndDevice endDevice;

    public Switch (@NotBlank String endDeviceMac, @NotNull Short switchNumber, @NotBlank String switchName) {
        this.endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
        this.switchNumber = switchNumber;
        this.switchName = switchName;
    }

    public Switch() {

    }

    public String getEndDeviceMac() {
        return endDeviceMac;
    }

    public void setEndDeviceMac(String endDeviceMac) {
        this.endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
    }

    public Short getSwitchNumber() {
        return switchNumber;
    }

    public void setSwitchNumber(Short switchNumber) {
        this.switchNumber = switchNumber;
    }

    public void setSwitchNumber(Integer switchNumber) {
        this.switchNumber = switchNumber.shortValue();
    }

    public @NotBlank String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(@NotBlank String switchName) {
        this.switchName = switchName;
    }

    public EndDevice getEndDevice() {
        return endDevice;
    }

    public void setEndDevice(EndDevice endDevice) {
        this.endDevice = endDevice;
    }
}
