package io.castelo.main_server.end_device;

import io.castelo.main_server.end_device_model.EndDeviceModel;
import io.castelo.main_server.end_device_sensor.Sensor;
import io.castelo.main_server.end_device_switch.Switch;
import io.castelo.main_server.gateway.Gateway;
import io.castelo.main_server.user.User;
import io.castelo.main_server.utils.MACAddressValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "end_devices")
public class EndDevice{

        @Id
        @NotBlank
        @Column(name = "end_device_mac", length = 17)
        private String endDeviceMac;

        @NotBlank
        @Column(name = "end_device_ip", nullable = false, columnDefinition = "inet")
        private String endDeviceIp;

        @NotNull
        @ManyToOne
        @JoinColumn(name = "model_id", nullable = false, referencedColumnName = "model_id")
        private EndDeviceModel endDeviceModel;

        @NotBlank
        @Column(name = "end_device_name", nullable = false, columnDefinition = "text")
        private String endDeviceName;

        @Column(name = "debug_mode")
        private boolean debugMode;

        @ManyToOne
        @JoinColumn(name = "user_id", referencedColumnName = "user_id")
        private User user;

        @ManyToOne
        @JoinColumn(name = "gateway_mac", referencedColumnName = "gateway_mac")
        private Gateway gateway;

        @NotBlank
        @Column(name = "firmware", nullable = false, columnDefinition = "text")
        private String firmware;

        @Enumerated(EnumType.STRING)
        @Column(name = "working_mode", columnDefinition = "device_mode")
        private DeviceMode working_mode;

        @OneToMany(mappedBy = "endDevice", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Sensor> sensors;

        @OneToMany(mappedBy = "endDevice", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Switch> switches;


        public EndDevice() {}

        public EndDevice(@NotBlank String endDeviceMac, @NotBlank String endDeviceIp, @NotNull EndDeviceModel endDeviceModel,
                         @NotBlank String endDeviceName, boolean debugMode, @NotNull User user, Gateway gateway,
                         @NotBlank String firmware, DeviceMode deviceMode) {

                this.endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
                this.endDeviceIp = endDeviceIp;
                this.endDeviceModel = endDeviceModel;
                this.endDeviceName = endDeviceName;
                this.debugMode = debugMode;
                this.user = user;
                this.gateway = gateway;
                this.firmware = firmware;
                this.working_mode = deviceMode;
        }

        public @NotBlank String getEndDeviceMac() {
                return endDeviceMac;
        }

        public void setEndDeviceMac(@NotBlank String endDeviceMac) {
                this.endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
        }

        public @NotBlank String getEndDeviceIp() {
                return endDeviceIp;
        }

        public void setEndDeviceIp(@NotBlank String endDeviceIp) {
                this.endDeviceIp = endDeviceIp;
        }

        public @NotNull EndDeviceModel getEndDeviceModel() {
                return endDeviceModel;
        }

        public void setEndDeviceModel(@NotNull EndDeviceModel endDeviceModel) {
                this.endDeviceModel = endDeviceModel;
        }

        public @NotBlank String getEndDeviceName() {
                return endDeviceName;
        }

        public void setEndDeviceName(@NotBlank String endDeviceName) {
                this.endDeviceName = endDeviceName;
        }

        public boolean isDebugMode() {
                return debugMode;
        }

        public void setDebugMode(boolean debugMode) {
                this.debugMode = debugMode;
        }

        public Gateway getGateway() {
                return gateway;
        }

        public void setGateway(Gateway gateway) {
                this.gateway = gateway;
        }

        public @NotBlank String getFirmware() {
                return firmware;
        }

        public void setFirmware(@NotBlank String firmware) {
                this.firmware = firmware;
        }

        public DeviceMode getWorkingMode() {
                return working_mode;
        }

        public void setWorkingMode(DeviceMode deviceMode) {
                this.working_mode = deviceMode;
        }

        public List<Sensor> getSensors() {
                return sensors;
        }

        public void setSensors(List<Sensor> sensors) {
                this.sensors = sensors;
        }

        public List<Switch> getSwitches() {
                return switches;
        }

        public void setSwitches(List<Switch> switches) {
                this.switches = switches;
        }

        public User getUser() {
                return user;
        }

        public void setUser(User user) {
                this.user = user;
        }
}
