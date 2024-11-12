package io.castelo.main_server.end_device_sensor_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/end-device/sensor-data")
public class SensorDataController {

    @Autowired
    private SensorDataService sensorDataService;

    @PostMapping
    public void addSensorData(@RequestBody EndDeviceSensorData endDeviceSensorData) {
        sensorDataService.addSensorData(endDeviceSensorData);
    }

    @GetMapping("/{endDeviceMac}")
    public Optional<EndDeviceSensorData> getSensorDataByMac(@PathVariable String endDeviceMac) {
        return sensorDataService.getSensorDataByMac(endDeviceMac);
    }
}