package io.castelo.main_server.end_device_sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensors")
public class SensorController {
    @Autowired
    private SensorService sensorService;

    @GetMapping
    public List<Sensor> getAllSensors() {
        return sensorService.getAllSensors();
    }

    @GetMapping("/{endDeviceMac}/{sensorNumber}")
    public ResponseEntity<Sensor> getSensor(@PathVariable String endDeviceMac, @PathVariable Integer sensorNumber) {
        SensorKey id = new SensorKey(endDeviceMac, sensorNumber.shortValue());
        Sensor sensor = sensorService.getSensor(id);
        return ResponseEntity.ok().body(sensor);
    }

    @PutMapping("/{endDeviceMac}/{sensorNumber}")
    public ResponseEntity<Sensor> updateSensor(@PathVariable String endDeviceMac, @PathVariable Integer sensorNumber, @RequestBody String sensorName) {
        SensorKey id = new SensorKey(endDeviceMac, sensorNumber.shortValue());
        return ResponseEntity.ok(sensorService.updateSensorName(id, sensorName));
    }
}
