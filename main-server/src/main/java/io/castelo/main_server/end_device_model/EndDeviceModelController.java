package io.castelo.main_server.end_device_model;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/end-device-models")
public class EndDeviceModelController {
    @Autowired
    private EndDeviceModelService endDeviceModelService;

    @GetMapping
    public List<EndDeviceModel> getAllModels() {
        return endDeviceModelService.getAllModels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EndDeviceModel> getModel(@PathVariable Integer id) {
        EndDeviceModel model = endDeviceModelService.getModel(id);
        return ResponseEntity.ok().body(model);
    }

    @PostMapping
    public EndDeviceModel createModel(@RequestBody @Valid EndDeviceModel model) {
        return endDeviceModelService.createModel(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EndDeviceModel> updateModel(@PathVariable Integer id, @RequestBody EndDeviceModel modelDetails) {
        return ResponseEntity.ok(endDeviceModelService.updateModel(id, modelDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Integer id) {
        endDeviceModelService.deleteModel(id);
        return ResponseEntity.noContent().build();
    }
}
