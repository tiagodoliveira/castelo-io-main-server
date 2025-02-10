package io.castelo.main_server.end_device;

import io.castelo.main_server.data_validators.IpAddressValidator;
import io.castelo.main_server.data_validators.MACAddressValidator;
import io.castelo.main_server.end_device_component.EndDeviceComponent;
import io.castelo.main_server.end_device_component.EndDeviceComponentService;
import io.castelo.main_server.exception.ResourceNotFoundException;
import io.castelo.main_server.gateway.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EndDeviceService {

    private final EndDeviceRepository endDeviceRepository;
    private final EndDeviceComponentService endDeviceComponentService;
    private final GatewayService gatewayService;

    @Autowired
    public EndDeviceService (EndDeviceRepository endDeviceRepository,
                             EndDeviceComponentService endDeviceComponentService,
                             GatewayService gatewayService) {
        this.endDeviceRepository = endDeviceRepository;
        this.endDeviceComponentService = endDeviceComponentService;
        this.gatewayService = gatewayService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional(readOnly = true)
    public List<EndDevice> getAllEndDevices() {
        return endDeviceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public EndDevice getEndDevice(String endDeviceMac) {
        return endDeviceRepository.findById(endDeviceMac)
                .orElseThrow(() -> new ResourceNotFoundException("EndDevice not found with mac: " + endDeviceMac));
    }

    @Transactional
    @PreAuthorize("#endDevice.getUser().getUsername() == authentication.name")
    public EndDevice createEndDevice(EndDevice endDevice) {
        IpAddressValidator.validateIpAddress(endDevice.getEndDeviceIp());
        MACAddressValidator.normalizeMACAddress(endDevice.getEndDeviceMac());

        gatewayService.verifyIfGatewayExists(endDevice.getGateway().getGatewayMac());

        if (endDevice.getWorkingMode() == null) {
            endDevice.setWorkingMode(WorkingModes.MANUAL);
        }
        endDevice = endDeviceRepository.save(endDevice);

        List<EndDeviceComponent> endDeviceComponents = endDeviceComponentService.createComponents(
                endDevice.getEndDeviceMac(), endDevice.getEndDeviceModel().getModelId());

        endDevice.setComponents(endDeviceComponents);

        return endDevice;
    }

    @Transactional
    public EndDevice updateEndDevice(EndDevice endDevice) {
        IpAddressValidator.validateIpAddress(endDevice.getEndDeviceIp());
        MACAddressValidator.normalizeMACAddress(endDevice.getEndDeviceMac());

        return endDeviceRepository.save(endDevice);
    }

    @Transactional
    @PreAuthorize("#endDevice.getUser().getUsername() == authentication.name or hasAuthority('ADMIN')")
    public void deleteEndDevice(EndDevice endDevice) {
        endDeviceRepository.delete(endDevice);
    }
}
