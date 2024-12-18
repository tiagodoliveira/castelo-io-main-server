package io.castelo.main_server.end_device_model;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/end-device-models")
public class EndDeviceModelController {

    private final EndDeviceModelService endDeviceModelService;

    @Autowired
    public EndDeviceModelController(EndDeviceModelService endDeviceModelService) {
        this.endDeviceModelService = endDeviceModelService;
    }

    @GetMapping
    public List<EndDeviceModel> getAllModels() {
        return endDeviceModelService.getAllModels();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EndDeviceModel getModel(@PathVariable Integer id) {
        return endDeviceModelService.getModel(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EndDeviceModel createModel(@RequestBody @Valid EndDeviceModel model) {
        return endDeviceModelService.createModel(model);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EndDeviceModel updateModel(@PathVariable Integer id, @RequestBody EndDeviceModel modelDetails) {
        return endDeviceModelService.updateModel(id, modelDetails);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteModel(@PathVariable Integer id) {
        endDeviceModelService.deleteModel(id);
    }
}
