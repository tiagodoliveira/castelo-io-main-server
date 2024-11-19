package io.castelo.main_server.gateway;

import io.castelo.main_server.user.User;
import io.castelo.main_server.utils.MACAddressValidator;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "Gateway")
public record Gateway(
        @Id
        String gatewayMac,

        @ManyToOne
        @JoinColumn(name = "gateway_user_id", nullable = false)
        User user,
        String gatewayIp,
        String gatewayName
) {
    public Gateway{
        // Validate and normalize gatewayMac
        if (gatewayMac == null || gatewayMac.isBlank()) {
            throw new IllegalArgumentException("Gateway Mac must not be blank");
        }
        gatewayMac = MACAddressValidator.normalizeMACAddress(gatewayMac);
    }
}
