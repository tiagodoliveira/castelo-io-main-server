package io.castelo.main_server.end_device;

import io.castelo.main_server.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EndDeviceService {

    @Autowired
    private EndDeviceRepository endDeviceRepository;

    public List<EndDevice> getAllEndDevices() {
        return endDeviceRepository.findAll();
    }

    public EndDevice getEndDevice(String endDeviceMac) {
        return endDeviceRepository.findById(endDeviceMac)
                .orElseThrow(() -> new ResourceNotFoundException("EndDevice not found with mac: " + endDeviceMac));
    }

    public EndDevice createEndDevice(EndDevice endDevice) {
        return endDeviceRepository.save(endDevice);
    }

    public EndDevice updateEndDevice(String endDeviceMac, EndDevice endDeviceDetails) {
        EndDevice endDevice = endDeviceRepository.findById(endDeviceMac)
                .orElseThrow(() -> new ResourceNotFoundException("EndDevice not found with mac: " + endDeviceMac));
        endDevice.setEndDeviceIp(endDeviceDetails.getEndDeviceIp());
        endDevice.setEndDeviceModel(endDeviceDetails.getEndDeviceModel());
        endDevice.setEndDeviceName(endDeviceDetails.getEndDeviceName());
        endDevice.setDebugMode(endDeviceDetails.isDebugMode());
        endDevice.setGateway(endDeviceDetails.getGateway());
        endDevice.setFirmware(endDeviceDetails.getFirmware());
        endDevice.setDeviceMode(endDeviceDetails.getWorkingMode());
        return endDeviceRepository.save(endDevice);
    }

    public void deleteEndDevice(String endDeviceMac) {
        EndDevice endDevice = endDeviceRepository.findById(endDeviceMac)
                .orElseThrow(() -> new ResourceNotFoundException("EndDevice not found with mac: " + endDeviceMac));
        endDeviceRepository.delete(endDevice);
    }
}
