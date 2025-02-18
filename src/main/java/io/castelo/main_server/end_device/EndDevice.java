package io.castelo.main_server.end_device;

import io.castelo.main_server.data_validators.MACAddressValidator;
import io.castelo.main_server.end_device_component.EndDeviceComponent;
import io.castelo.main_server.end_device_model.EndDeviceModel;
import io.castelo.main_server.gateway.Gateway;
import io.castelo.main_server.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "end_devices")
public class EndDevice{

        @Id
        @NotBlank
        @Column(name = "end_device_mac", length = 17)
        private String endDeviceMac;

        @Column(name = "end_device_ip", columnDefinition = "inet")
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

        @NotNull
        @ManyToOne
        @JoinColumn(name = "owner_id", referencedColumnName = "user_id", nullable = false)
        private User owner;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "end_device_users",
                joinColumns = @JoinColumn(name = "end_device_mac"),
                inverseJoinColumns = @JoinColumn(name = "user_id")
        )
        private Set<User> sharedUsers = new HashSet<>();

        @ManyToOne
        @JoinColumn(name = "gateway_mac", referencedColumnName = "gateway_mac")
        private Gateway gateway;

        @NotBlank
        @Column(name = "firmware", nullable = false, columnDefinition = "text")
        private String firmware;

        @Enumerated(EnumType.STRING)
        @Column(name = "working_mode", columnDefinition = "working_modes")
        private WorkingModes working_mode;

        @OneToMany(mappedBy = "endDevice", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
        private List<EndDeviceComponent> endDeviceComponents = new ArrayList<>();


        public EndDevice() {}

        public EndDevice(@NotBlank String endDeviceMac, String endDeviceIp, @NotNull EndDeviceModel endDeviceModel,
                         @NotBlank String endDeviceName, boolean debugMode, @NotNull User user, Gateway gateway,
                         @NotBlank String firmware, WorkingModes workingModes) {

                this.endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
                this.endDeviceIp = endDeviceIp;
                this.endDeviceModel = endDeviceModel;
                this.endDeviceName = endDeviceName;
                this.debugMode = debugMode;
                this.owner = user;
                this.gateway = gateway;
                this.firmware = firmware;
                this.working_mode = workingModes;
                this.endDeviceComponents = new ArrayList<>();
                this.sharedUsers = new HashSet<>();
        }

        public boolean hasAccess(User user) {
                return owner.equals(user) || sharedUsers.contains(user);
        }

        public @NotBlank String getEndDeviceMac() {
                return endDeviceMac;
        }

        public void setEndDeviceMac(@NotBlank String endDeviceMac) {
                this.endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
        }

        public String getEndDeviceIp() {
                return endDeviceIp;
        }

        public void setEndDeviceIp(String endDeviceIp) {
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

        public User getOwner() {
                return owner;
        }

        public void setOwner(User user) {
                this.owner = user;
        }

        public Set<User> getSharedUsers() {
                return sharedUsers;
        }

        public void setSharedUsers(Set<User> sharedUsers) {
                this.sharedUsers = sharedUsers;
        }

        public void addSharedUser(User user) {
                this.sharedUsers.add(user);
        }

        public void removeSharedUser(User user) {
                this.sharedUsers.remove(user);
        }


        @Override
        public String toString() {
                return "EndDevice{" +
                        "endDeviceMac='" + endDeviceMac + '\'' +
                        ", endDeviceIp='" + endDeviceIp + '\'' +
                        ", endDeviceModel=" + (endDeviceModel != null ? endDeviceModel.getModelId() : null) +
                        ", endDeviceName='" + endDeviceName + '\'' +
                        ", debugMode=" + debugMode +
                        ", user=" + (owner != null ? owner.getUserId() : null) +
                        ", gateway=" + (gateway != null ? gateway.getGatewayMac() : null) +
                        ", firmware='" + firmware + '\'' +
                        ", working_mode=" + working_mode +
                        ", endDeviceComponents=" + (endDeviceComponents != null ? endDeviceComponents.size() : 0) +
                        '}';
        }
}
