package io.castelo.main_server.gateway;

import io.castelo.main_server.end_device.EndDeviceService;
import io.castelo.main_server.exception.ResourceNotFoundException;
import io.castelo.main_server.utils.IpAddressValidator;
import io.castelo.main_server.utils.MACAddressValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GatewayService {
    
    private final GatewayRepository gatewayRepository;
    private final EndDeviceService endDeviceService;

    @Autowired
    public GatewayService(GatewayRepository gatewayRepository, EndDeviceService endDeviceService) {
        this.gatewayRepository = gatewayRepository;
        this.endDeviceService = endDeviceService;
    }

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
        endDeviceService.deleteAllGatewayConnectedEndDevices(gatewayMac);
        gatewayRepository.delete(gateway);
    }
}
