package io.castelo.main_server.gateway;

import io.castelo.main_server.data_validators.IpAddressValidator;
import io.castelo.main_server.data_validators.MACAddressValidator;
import io.castelo.main_server.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gateways")
public class Gateway{

    @Id
    @Column(name = "gateway_mac", length = 17)
    String gatewayMac;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false, referencedColumnName = "user_id")
    User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "gateway_shared_users",
            joinColumns = @JoinColumn(name = "gateway_mac"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> sharedUsers = new HashSet<>();

    @NotBlank
    @Column(name = "gateway_ip", columnDefinition = "inet")
    String gatewayIp;

    @NotBlank
    @Column(name = "gateway_name", columnDefinition = "text")
    String gatewayName;

    public Gateway(@NotBlank String gatewayMac, @NotNull User user, @NotBlank String gatewayIp, @NotBlank String gatewayName) {
        this.gatewayMac = gatewayMac;
        this.owner = user;
        this.gatewayIp = gatewayIp;
        this.gatewayName = gatewayName;
        this.sharedUsers = new HashSet<>();
    }

    public Gateway() {

    }

    public boolean hasAccess(User user) {
        return owner.equals(user) || sharedUsers.contains(user);
    }

    public String getGatewayMac() {
        return gatewayMac;
    }

    public void setGatewayMac(@NotBlank String gatewayMac) {
        this.gatewayMac = MACAddressValidator.normalizeMACAddress(gatewayMac);
    }

    public @NotBlank String getGatewayIp() {
        return gatewayIp;
    }

    public void setGatewayIp(@NotBlank String gatewayIp) {
        IpAddressValidator.validateIpAddress(gatewayIp);
        this.gatewayIp = gatewayIp;
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

    public @NotBlank String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(@NotBlank String gatewayName) {
        this.gatewayName = gatewayName;
    }

    @Override
    public String toString() {
        return "Gateway{" +
                "gatewayMac='" + gatewayMac + '\'' +
                ", owner=" + owner +
                ", gatewayIp='" + gatewayIp + '\'' +
                ", gatewayName='" + gatewayName + '\'' +
                '}';
    }
}
