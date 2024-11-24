package io.castelo.main_server.gateway;

import io.castelo.main_server.user.User;
import io.castelo.main_server.utils.IpAddressValidator;
import io.castelo.main_server.utils.MACAddressValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "gateways")
public class Gateway{

    @Id
    @Column(name = "gateway_mac", length = 17)
    String gatewayMac;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "gateway_user_id", nullable = false, referencedColumnName = "user_id")
    User user;

    @NotBlank
    @Column(name = "gateway_ip", columnDefinition = "inet")
    String gatewayIp;

    @NotBlank
    @Column(name = "gateway_name", columnDefinition = "text")
    String gatewayName;

    public Gateway(@NotBlank String gatewayMac, @NotNull User user, @NotBlank String gatewayIp, @NotBlank String gatewayName) {
        this.gatewayMac = gatewayMac;
        this.user = user;
        this.gatewayIp = gatewayIp;
        this.gatewayName = gatewayName;
    }

    public Gateway() {

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

    public @NotNull User getUser() {
        return user;
    }

    public void setUser(@NotNull User user) {
        this.user = user;
    }

    public @NotBlank String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(@NotBlank String gatewayName) {
        this.gatewayName = gatewayName;
    }
}
