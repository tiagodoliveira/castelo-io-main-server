package io.castelo.main_server.end_device;

import io.castelo.main_server.end_device_component.EndDeviceComponent;
import io.castelo.main_server.end_device_component.EndDeviceComponentService;
import io.castelo.main_server.exception.ResourceNotFoundException;
import io.castelo.main_server.gateway.GatewayService;
import io.castelo.main_server.user.UserService;
import io.castelo.main_server.utils.IpAddressValidator;
import io.castelo.main_server.utils.MACAddressValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EndDeviceService {

    private final EndDeviceRepository endDeviceRepository;
    private final EndDeviceComponentService endDeviceComponentService;
    private final UserService userService;
    private final GatewayService gatewayService;

    @Autowired
    public EndDeviceService (EndDeviceRepository endDeviceRepository,
                             EndDeviceComponentService endDeviceComponentService,
                             UserService userService,
                             GatewayService gatewayService) {
        this.endDeviceRepository = endDeviceRepository;
        this.endDeviceComponentService = endDeviceComponentService;
        this.userService = userService;
        this.gatewayService = gatewayService;
    }

    public List<EndDevice> getAllEndDevices() {
        return endDeviceRepository.findAll();
    }

    public EndDevice getEndDevice(String endDeviceMac) {
        return endDeviceRepository.findById(endDeviceMac)
                .orElseThrow(() -> new ResourceNotFoundException("EndDevice not found with mac: " + endDeviceMac));
    }

    @Transactional
    public EndDevice createEndDevice(EndDevice endDevice) {
        IpAddressValidator.validateIpAddress(endDevice.getEndDeviceIp());
        MACAddressValidator.normalizeMACAddress(endDevice.getEndDeviceMac());

        userService.verifyIfUserExists(endDevice.getUser().getUsername());
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
    public EndDevice updateEndDevice(String endDeviceMac, EndDeviceDTO endDeviceDTO) {
        EndDevice endDevice = getEndDevice(endDeviceMac);
        EndDeviceMapper.INSTANCE.updateEndDeviceFromDto(endDeviceDTO, endDevice);

        IpAddressValidator.validateIpAddress(endDevice.getEndDeviceIp());
        MACAddressValidator.normalizeMACAddress(endDevice.getEndDeviceMac());

        userService.verifyIfUserExists(endDevice.getUser().getUsername());
        gatewayService.verifyIfGatewayExists(endDevice.getGateway().getGatewayMac());

        return endDeviceRepository.save(endDevice);
    }

    @Transactional
    public void deleteEndDevice(String endDeviceMac) {
        EndDevice endDevice = getEndDevice(endDeviceMac);
        endDeviceRepository.delete(endDevice);
    }
}
