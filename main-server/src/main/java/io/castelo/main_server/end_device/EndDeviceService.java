package io.castelo.main_server.end_device;

import io.castelo.main_server.end_device_sensor.SensorService;
import io.castelo.main_server.end_device_switch.SwitchService;
import io.castelo.main_server.exception.ResourceNotFoundException;
import io.castelo.main_server.utils.IpAddressValidator;
import io.castelo.main_server.utils.MACAddressValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EndDeviceService {

    private final EndDeviceRepository endDeviceRepository;
    private final SwitchService switchService;
    private final SensorService sensorService;

    @Autowired
    public EndDeviceService (EndDeviceRepository endDeviceRepository, SwitchService switchService, SensorService sensorService) {
        this.endDeviceRepository = endDeviceRepository;
        this.switchService = switchService;
        this.sensorService = sensorService;
    }

    public List<EndDevice> getAllEndDevices() {
        return endDeviceRepository.findAll();
    }

    public EndDevice getEndDevice(String endDeviceMac) {
        return endDeviceRepository.findById(endDeviceMac)
                .orElseThrow(() -> new ResourceNotFoundException("EndDevice not found with mac: " + endDeviceMac));
    }

    public EndDevice createEndDevice(EndDevice endDevice) {
        IpAddressValidator.validateIpAddress(endDevice.getEndDeviceIp());
        MACAddressValidator.normalizeMACAddress(endDevice.getEndDeviceMac());

        if (endDevice.getWorkingMode() == null) {
            endDevice.setWorkingMode(DeviceMode.MANUAL);
        }
        return endDeviceRepository.save(endDevice);
    }

    @Transactional
    public EndDevice updateEndDevice(String endDeviceMac, EndDeviceDTO endDeviceDTO) {
        EndDevice endDevice = getEndDevice(endDeviceMac);
        EndDeviceMapper.INSTANCE.updateEndDeviceFromDto(endDeviceDTO, endDevice);

        return endDeviceRepository.save(endDevice);
    }

    @Transactional
    public void deleteEndDevice(String endDeviceMac) {
        EndDevice endDevice = getEndDevice(endDeviceMac);
        switchService.deleteAllSwitches(endDeviceMac);
        sensorService.deleteAllSensors(endDeviceMac);
        endDeviceRepository.delete(endDevice);
    }

    @Transactional
    public void deleteAllGatewayConnectedEndDevices(String gatewayMac) {
        List<EndDevice> endDevices = endDeviceRepository.findAllByGateway_GatewayMac(gatewayMac);
        endDevices.forEach(endDevice -> {
            switchService.deleteAllSwitches(endDevice.getEndDeviceMac());
            sensorService.deleteAllSensors(endDevice.getEndDeviceMac());
        });
        endDeviceRepository.deleteAll(endDevices);
    }
}
