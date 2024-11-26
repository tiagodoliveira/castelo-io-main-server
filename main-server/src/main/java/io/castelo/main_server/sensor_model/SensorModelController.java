package io.castelo.main_server.sensor_model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensor-models")
public class SensorModelController {

    @Autowired
    private SensorModelService sensorModelService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SensorModel> getAllSensors() {
        return sensorModelService.getAllSensorModels();
    }

    @GetMapping("/{modelId}")
    @ResponseStatus(HttpStatus.OK)
    public List<SensorModel> getSensorModel(@PathVariable Integer modelId) {
        return sensorModelService.getSensorModel(modelId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createSensorModels(@RequestBody List<SensorModel> sensorModel) {
        sensorModelService.createSensors(sensorModel);
    }
}
