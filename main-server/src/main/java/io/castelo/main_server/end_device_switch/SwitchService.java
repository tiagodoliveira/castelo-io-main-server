package io.castelo.main_server.end_device_switch;

import io.castelo.main_server.exception.ResourceNotFoundException;
import io.castelo.main_server.switch_model.SwitchModel;
import io.castelo.main_server.switch_model.SwitchModelService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwitchService {


    private final SwitchRepository switchRepository;
    private final SwitchModelService switchModelService;

    @Autowired
    public SwitchService(SwitchRepository switchRepository, SwitchModelService switchModelService) {
        this.switchRepository = switchRepository;
        this.switchModelService = switchModelService;
    }

    public List<Switch> getAllSwitches() {
        return switchRepository.findAll();
    }

    public Switch getSwitch(SwitchKey id) {
        return switchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Switch not found with id: " + id));
    }

    public List<Switch> createSwitches(@NotEmpty String endDeviceMac, @NotNull Integer modelId) {
        List<SwitchModel> switchModels = switchModelService.getAllSwitchModelsByModelId(modelId);
        List<Switch> switches = switchModels.stream().map(switchModel -> new Switch(endDeviceMac, switchModel.getSwitchNumber(), switchModel.getSwitchName())).toList();
        return switchRepository.saveAll(switches);
    }

    public Switch updateSwitchName(SwitchKey id, String switchName) {
        Switch sw = switchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Switch not found with id: " + id));
        sw.setSwitchName(switchName);
        return switchRepository.save(sw);
    }
}
