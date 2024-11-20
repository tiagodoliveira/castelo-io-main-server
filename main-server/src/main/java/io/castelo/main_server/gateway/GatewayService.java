package io.castelo.main_server.gateway;

import io.castelo.main_server.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GatewayService {
    
    private final GatewayRepository gatewayRepository;

    @Autowired
    public GatewayService(GatewayRepository gatewayRepository) {
        this.gatewayRepository = gatewayRepository;
    }

    public List<Gateway> getAllGateways() {
        return gatewayRepository.findAll();
    }

    public Gateway getGateway(String gatewayMac) {
        return gatewayRepository.findById(gatewayMac)
                .orElseThrow(() -> new ResourceNotFoundException("Gateway not found with mac: " + gatewayMac));
    }

    public Gateway createGateway(Gateway gateway) {
        return gatewayRepository.save(gateway);
    }

    public Gateway updateGateway(String gatewayMac, Gateway gatewayDetails) {
        Gateway gateway = gatewayRepository.findById(gatewayMac)
                .orElseThrow(() -> new ResourceNotFoundException("Gateway not found with mac: " + gatewayMac));
        gateway.setGatewayName(gatewayDetails.getGatewayName());
        gateway.setGatewayIp(gatewayDetails.getGatewayIp());
        return gatewayRepository.save(gateway);
    }

    public void deleteGateway(String gatewayMac) {
        Gateway gateway = gatewayRepository.findById(gatewayMac)
                .orElseThrow(() -> new ResourceNotFoundException("Gateway not found with mac: " + gatewayMac));
        gatewayRepository.delete(gateway);
    }
}
