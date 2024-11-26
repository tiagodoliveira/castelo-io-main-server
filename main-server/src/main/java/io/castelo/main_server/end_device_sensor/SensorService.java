package io.castelo.main_server.end_device_sensor;

import io.castelo.main_server.exception.ResourceNotFoundException;
import io.castelo.main_server.sensor_model.SensorModel;
import io.castelo.main_server.sensor_model.SensorModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;
    private final SensorModelService sensorModelService;

    @Autowired
    public SensorService(SensorRepository sensorRepository, SensorModelService sensorModelService) {
        this.sensorRepository = sensorRepository;
        this.sensorModelService = sensorModelService;
    }

    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    public Sensor getSensor(SensorKey id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor not found with id: " + id));
    }

    public List<Sensor> createSensors(String endDeviceMac, Integer modelId) {
        List<SensorModel> sensorModels = sensorModelService.getAllSensorModelsByModelId(modelId);
        List<Sensor> sensors = sensorModels.stream().map(sensorModel -> new Sensor(endDeviceMac, sensorModel.getSensorNumber(), sensorModel.getSensorName())).toList();
        return sensorRepository.saveAll(sensors);
    }

    public Sensor updateSensorName(SensorKey id, String sensorName) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor not found with id: " + id));
        sensor.setSensorName(sensorName);
        return sensorRepository.save(sensor);
    }
}
