package io.castelo.main_server.sensor_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/end-device/sensor-data")
public class SensorDataController {

    private final SensorDataService sensorDataService;

    @Autowired
    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @PostMapping("/add-sensor-value")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSensorValue(@RequestBody SensorData sensorData) {
        sensorDataService.saveSensorData(sensorData);
    }

    @GetMapping("/{endDeviceMac}")
    @ResponseStatus(HttpStatus.OK)
    public List<SensorData> getSensorValuesByEndDeviceMac(@PathVariable String endDeviceMac) {
        return sensorDataService.findSensorValuesByEndDeviceMac(endDeviceMac);
    }

    @PostMapping("/add-sensor-values")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSensorValues(@RequestBody List<SensorData> sensorDataList) {
        sensorDataService.saveAllSensorData(sensorDataList);
    }
}