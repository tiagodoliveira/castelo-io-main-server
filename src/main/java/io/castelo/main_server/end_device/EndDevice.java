package io.castelo.main_server.end_device;

import io.castelo.main_server.end_device_component.EndDeviceComponent;
import io.castelo.main_server.end_device_model.EndDeviceModel;
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
        @Column(name = "working_mode", columnDefinition = "working_modes")
        private WorkingModes working_mode;

        @OneToMany(mappedBy = "endDevice", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<EndDeviceComponent> endDeviceComponents;


        public EndDevice() {}

        public EndDevice(@NotBlank String endDeviceMac, @NotBlank String endDeviceIp, @NotNull EndDeviceModel endDeviceModel,
                         @NotBlank String endDeviceName, boolean debugMode, @NotNull User user, Gateway gateway,
                         @NotBlank String firmware, WorkingModes workingModes) {

                this.endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
                this.endDeviceIp = endDeviceIp;
                this.endDeviceModel = endDeviceModel;
                this.endDeviceName = endDeviceName;
                this.debugMode = debugMode;
                this.user = user;
                this.gateway = gateway;
                this.firmware = firmware;
                this.working_mode = workingModes;
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

        public WorkingModes getWorkingMode() {
                return working_mode;
        }

        public void setWorkingMode(WorkingModes workingModes) {
                this.working_mode = workingModes;
        }

        public List<EndDeviceComponent> getComponents() {
                return endDeviceComponents;
        }

        public void setComponents(List<EndDeviceComponent> endDeviceComponents) {
                this.endDeviceComponents = endDeviceComponents;
        }

        public User getUser() {
                return user;
        }

        public void setUser(User user) {
                this.user = user;
        }


        @Override
        public String toString() {
                return "EndDevice{" +
                        "endDeviceMac='" + endDeviceMac + '\'' +
                        ", endDeviceIp='" + endDeviceIp + '\'' +
                        ", endDeviceModel=" + (endDeviceModel != null ? endDeviceModel.getModelId() : null) +
                        ", endDeviceName='" + endDeviceName + '\'' +
                        ", debugMode=" + debugMode +
                        ", user=" + (user != null ? user.getUserId() : null) +
                        ", gateway=" + (gateway != null ? gateway.getGatewayMac() : null) +
                        ", firmware='" + firmware + '\'' +
                        ", working_mode=" + working_mode +
                        ", endDeviceComponents=" + (endDeviceComponents != null ? endDeviceComponents.size() : 0) +
                        '}';
        }
}
