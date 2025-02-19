package io.castelo.main_server.gateway;

import io.castelo.main_server.data_validators.IpAddressValidator;
import io.castelo.main_server.data_validators.MACAddressValidator;
import io.castelo.main_server.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GatewayService {
    
    private final GatewayRepository gatewayRepository;

    @Autowired
    public GatewayService(GatewayRepository gatewayRepository) {
        this.gatewayRepository = gatewayRepository;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Gateway> getAllGateways() {
        return gatewayRepository.findAll();
    }

    public Gateway getGateway(String gatewayMac) {
        return gatewayRepository.findById(gatewayMac)
                .orElseThrow(() -> new ResourceNotFoundException("Gateway not found with mac: " + gatewayMac));
    }

    public Gateway createGateway(Gateway gateway) {
        IpAddressValidator.validateIpAddress(gateway.getGatewayIp());
        MACAddressValidator.normalizeMACAddress(gateway.getGatewayMac());

        return gatewayRepository.save(gateway);
    }

    public Gateway updateGateway(String gatewayMac, Gateway gatewayDetails) {
        Gateway gateway = gatewayRepository.findById(gatewayMac)
                .orElseThrow(() -> new ResourceNotFoundException("Gateway not found with mac: " + gatewayMac));

        IpAddressValidator.validateIpAddress(gatewayDetails.getGatewayIp());
        gateway.setGatewayIp(gatewayDetails.getGatewayIp());

        if ( gatewayDetails.getGatewayName() != null)
            gateway.setGatewayName(gatewayDetails.getGatewayName());

        return gatewayRepository.save(gateway);
    }

    public void deleteGateway(String gatewayMac) {
        Gateway gateway = gatewayRepository.findById(gatewayMac)
                .orElseThrow(() -> new ResourceNotFoundException("Gateway not found with mac: " + gatewayMac));
        gatewayRepository.delete(gateway);
    }

    public void verifyIfGatewayExists(String gatewayMac) {
        if(!gatewayRepository.existsById(gatewayMac))
            throw new ResourceNotFoundException("Gateway not found with mac: " + gatewayMac);
    }
}
