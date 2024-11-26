package io.castelo.main_server.end_device_sensor;

import io.castelo.main_server.end_device.EndDevice;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@IdClass(SensorKey.class)
@Table(name = "sensors")
public class Sensor {

    @Id
    @NotBlank
    @Column(name = "end_device_mac", length = 17)
    private String endDeviceMac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_device_mac", insertable = false, updatable = false)
    private EndDevice endDevice;

    @Id
    @Column(name = "sensor_number")
    private Short sensorNumber;

    @NotBlank
    @Column(name = "sensor_name", nullable = false, columnDefinition = "text")
    private String sensorName;


    public Sensor (@NotBlank String endDeviceMac, @NotNull Short sensorNumber, @NotBlank String sensorName){
        this.endDeviceMac = endDeviceMac;
        this.sensorNumber = sensorNumber;
        this.sensorName = sensorName;
    }

    public Sensor() {

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

    public @NotBlank String getSensorName() {
        return sensorName;
    }

    public void setSensorName(@NotBlank String sensorName) {
        this.sensorName = sensorName;
    }
}
