package io.castelo.main_server.switch_model;

import io.castelo.main_server.exception.ResourceNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwitchModelService {

    private final SwitchModelRepository switchModelRepository;

    @Autowired
    public SwitchModelService(SwitchModelRepository switchModelRepository) {
        this.switchModelRepository = switchModelRepository;
    }

    public List<SwitchModel> getAllSwitchModels() {
        return switchModelRepository.findAll();
    }

    public List<SwitchModel> getSwitchModel(Integer modelId) {
        List<SwitchModel> switchModel = switchModelRepository.findAllByModelId(modelId);
        if (switchModel.isEmpty())
            throw new ResourceNotFoundException("SwitchModel not found with model id: " + modelId);
        return switchModel;
    }

    public void createSwitches(@NotNull List<SwitchModel> switchModels) {
        switchModelRepository.saveAll(switchModels);
    }

    public List<SwitchModel> getAllSwitchModelsByModelId(@NotNull Integer modelId) {
        return switchModelRepository.findAllByModelId(modelId);
    }
}
