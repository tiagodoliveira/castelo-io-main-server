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
    @PreAuthorize("#endDevice.getUser().userId == authentication.principal.userId")
    public EndDevice createEndDevice(EndDevice endDevice) {
        validateEndDevice(endDevice);

        if (endDevice.getWorkingMode() == null) {
            endDevice.setWorkingMode(WorkingModes.AUTONOMOUS);
        }

        endDevice = endDeviceRepository.save(endDevice);

        List<EndDeviceComponent> endDeviceComponents = endDeviceComponentService.createComponents(endDevice);

        endDevice.getComponents().clear();
        endDevice.getComponents().addAll(endDeviceComponents);

        return endDevice;
    }

    @Transactional
    public EndDevice updateEndDevice(EndDevice endDevice) {
        validateEndDevice(endDevice);
        return endDeviceRepository.save(endDevice);
    }

    @Transactional
    @PreAuthorize("#endDevice.getUser().getUsername() == authentication.name or hasAuthority('ADMIN')")
    public void deleteEndDevice(EndDevice endDevice) {
        endDeviceRepository.delete(endDevice);
    }

    @Transactional
    public void pairWithGateway(String endDeviceMac, String gatewayMac) {
        EndDevice endDevice = endDeviceRepository.findById(endDeviceMac)
                .orElseThrow(() -> new ResourceNotFoundException("EndDevice not found with mac: " + endDeviceMac));
        endDevice.setGateway(gatewayService.getGateway(gatewayMac));
        endDeviceRepository.save(endDevice);
    }

    private void validateEndDevice(EndDevice endDevice) {
        IpAddressValidator.validateIpAddress(endDevice.getEndDeviceIp());
        endDevice.setEndDeviceMac(MACAddressValidator.normalizeMACAddress(endDevice.getEndDeviceMac()));

        if(endDevice.getGateway() != null)
            gatewayService.verifyIfGatewayExists(endDevice.getGateway().getGatewayMac());
    }
}
