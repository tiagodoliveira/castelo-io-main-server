package io.castelo.main_server.switch_model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/switch-models")
public class SwitchModelController {

    @Autowired
    private SwitchModelService switchModelService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SwitchModel> getAllSwitchModels() {
        return switchModelService.getAllSwitchModels();
    }

    @GetMapping("/{modelId}")
    @ResponseStatus(HttpStatus.OK)
    public List<SwitchModel> getSwitchModel(@PathVariable int modelId) {
        return switchModelService.getSwitchModel(modelId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createSensor(@RequestBody List<SwitchModel> switchModel) {
        switchModelService.createSwitches(switchModel);
    }
}
