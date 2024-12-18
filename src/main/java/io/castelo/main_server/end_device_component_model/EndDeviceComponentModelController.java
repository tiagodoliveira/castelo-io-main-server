package io.castelo.main_server.end_device_component_model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/end-device-component-models")
public class EndDeviceComponentModelController {

    private final EndDeviceComponentModelService endDeviceComponentModelService;

    @Autowired
    public EndDeviceComponentModelController(EndDeviceComponentModelService endDeviceComponentModelService) {
        this.endDeviceComponentModelService = endDeviceComponentModelService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EndDeviceComponentModel> getAllComponents() {
        return endDeviceComponentModelService.getAllComponentModels();
    }

    @GetMapping("/{modelId}")
    @ResponseStatus(HttpStatus.OK)
    public List<EndDeviceComponentModel> getComponentModel(@PathVariable Integer modelId) {
        return endDeviceComponentModelService.getComponentModel(modelId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<EndDeviceComponentModel> createComponentModels(@RequestBody List<EndDeviceComponentModel> endDeviceComponentModel) {
        return endDeviceComponentModelService.createComponents(endDeviceComponentModel);
    }
}
