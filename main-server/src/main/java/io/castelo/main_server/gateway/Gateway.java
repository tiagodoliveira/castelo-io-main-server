package io.castelo.main_server.gateway;

import io.castelo.main_server.utils.MACAddressValidator;
import org.springframework.data.annotation.Id;

public record Gateway(
        @Id
        String gatewayMac,
        int gatewayUserId,
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
