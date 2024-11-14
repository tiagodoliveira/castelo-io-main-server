package io.castelo.main_server.end_device_data;

import io.castelo.main_server.end_device_data.sensor_data.SensorValueRequest;
import io.castelo.main_server.end_device_data.switch_data.SwitchValueDBEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/end-device/sensor-data")
public class EndDeviceDataController {

    private final EndDeviceDataService endDeviceDataService;

    @Autowired
    public EndDeviceDataController(EndDeviceDataService endDeviceDataService) {
        this.endDeviceDataService = endDeviceDataService;
    }

    @GetMapping("/{endDeviceMac}")
    @ResponseStatus(HttpStatus.OK)
    public EndDeviceData getEndDeviceDataByMac(@PathVariable String endDeviceMac) {
        return endDeviceDataService.findByEndDeviceMac(endDeviceMac);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createEndDeviceData(@RequestBody EndDeviceData endDeviceData) {
        endDeviceDataService.saveEndDeviceData(endDeviceData);
    }

    @PostMapping("/add-sensor-value")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSensorValue(@RequestBody SensorValueRequest sensorValueRequest) {
        endDeviceDataService.saveSensorValue(sensorValueRequest);
    }

    @PostMapping("/add-switch-value")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSwitchValue(@RequestBody SwitchValueDBEntry switchValueDBEntry) {
        endDeviceDataService.saveSwitchValue(switchValueDBEntry);
    }
}