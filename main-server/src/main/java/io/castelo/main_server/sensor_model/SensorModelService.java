package io.castelo.main_server.sensor_model;

import io.castelo.main_server.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorModelService {

    private final SensorModelRepository sensorModelRepository;

    @Autowired
    public SensorModelService(SensorModelRepository sensorModelRepository) {
        this.sensorModelRepository = sensorModelRepository;
    }

    public List<SensorModel> getAllSensorModels() {
        return sensorModelRepository.findAll();
    }

    public List<SensorModel> getSensorModel(Integer modelId) {
        List<SensorModel> sensorModels = sensorModelRepository.findAllByModelId(modelId);
        if (sensorModels.isEmpty())
            throw new ResourceNotFoundException("SensorModel not found with model id: " + modelId);
        return sensorModels;
    }

    public void createSensors(List<SensorModel> sensorModel) {
        sensorModelRepository.saveAll(sensorModel);
    }

    public List<SensorModel> getAllSensorModelsByModelId(Integer modelId) {
        return sensorModelRepository.findAllByModelId(modelId);
    }
}
