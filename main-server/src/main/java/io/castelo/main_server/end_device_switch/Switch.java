package io.castelo.main_server.end_device_switch;

import io.castelo.main_server.end_device.EndDevice;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@IdClass(SwitchKey.class)
@Table(name = "switches")
public class Switch{

    @Id
    @NotBlank
    @Column(name = "end_device_mac", length = 17)
    private String endDeviceMac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_device_mac", insertable = false, updatable = false)
    private EndDevice endDevice;

    @Id
    @Column(name = "switch_number")
    Short switchNumber;

    @NotBlank
    @Column(name = "switch_name", nullable = false, columnDefinition = "text")
    String switchName;

    public Switch (@NotEmpty String endDeviceMac, @NotNull Short switchNumber, @NotBlank String switchName) {
        this.endDeviceMac = endDeviceMac;
        this.switchNumber = switchNumber;
        this.switchName = switchName;
    }

    public Switch() {

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

    public void setSwitchNumber(Integer switchNumber) {
        this.switchNumber = switchNumber.shortValue();
    }

    public @NotBlank String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(@NotBlank String switchName) {
        this.switchName = switchName;
    }

}
