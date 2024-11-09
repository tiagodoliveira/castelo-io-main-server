package io.castelo.main_server.end_device_sensor_data;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/enddevice/sensor")
public class SensorDataController {

    private final SensorDataRepository sensorDataRepository;

    public SensorDataController(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add-sensor-data")
    public void addSensorData(@Valid @RequestBody EndDeviceSensorData endDeviceSensorData) {
        sensorDataRepository.addSensorData(endDeviceSensorData);
    }

    @GetMapping("/enddevice/{macAddress}/sensor/{sensorNumber}")
    public ResponseEntity<EndDeviceSensorData> getSensorData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime stop,
            @PathVariable String macAddress,
            @PathVariable int sensorNumber) {

        EndDeviceSensorData sensorData = sensorDataRepository.getSensorDataBySensorNumber(start, stop, macAddress, sensorNumber);

        if (sensorData == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(sensorData, HttpStatus.OK);
    }

    @GetMapping("/enddevice/{macAddress}")
    public ResponseEntity<EndDeviceSensorData> getAllSensorData(@PathVariable String macAddress) {
        EndDeviceSensorData sensorData = sensorDataRepository.getAllSensorValues(macAddress);

        if (sensorData == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(sensorData, HttpStatus.OK);
    }
}