package io.castelo.main_server.end_device_model;

import io.castelo.main_server.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EndDeviceModelService {

    @Autowired
    private EndDeviceModelRepository endDeviceModelRepository;

    public List<EndDeviceModel> getAllModels() {
        return endDeviceModelRepository.findAll();
    }

    public EndDeviceModel getModel(Integer modelId) {
        return endDeviceModelRepository.findById(modelId)
                .orElseThrow(() -> new ResourceNotFoundException("Model not found with id: " + modelId));
    }

    @Transactional
    public EndDeviceModel createModel(EndDeviceModel model) {
        return endDeviceModelRepository.save(model);
    }

    public EndDeviceModel updateModel(Integer modelId, EndDeviceModel modelDetails) {
        EndDeviceModel model = endDeviceModelRepository.findById(modelId)
                .orElseThrow(() -> new ResourceNotFoundException("Model not found with id: " + modelId));
        model.setLatestFirmwareVersion(modelDetails.getLatestFirmwareVersion());
        return endDeviceModelRepository.save(model);
    }

    public void deleteModel(Integer modelId) {
        EndDeviceModel model = endDeviceModelRepository.findById(modelId)
                .orElseThrow(() -> new ResourceNotFoundException("Model not found with id: " + modelId));
        endDeviceModelRepository.delete(model);
    }
}
