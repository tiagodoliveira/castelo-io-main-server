package io.castelo.main_server.end_device_component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/end-device-components")
public class EndDeviceComponentController {

    private final EndDeviceComponentService endDeviceComponentService;

    @Autowired
    public EndDeviceComponentController(EndDeviceComponentService endDeviceComponentService) {
        this.endDeviceComponentService = endDeviceComponentService;
    }

    @GetMapping("/{endDeviceMac}/{componentNumber}")
    public EndDeviceComponent getComponent(@PathVariable String endDeviceMac, @PathVariable Integer componentNumber) {
        EndDeviceComponentKey id = new EndDeviceComponentKey(endDeviceMac, componentNumber.shortValue());
        return endDeviceComponentService.getComponent(id);
    }

    @PutMapping("/{endDeviceMac}/{componentNumber}")
    public EndDeviceComponent updateComponent(@PathVariable String endDeviceMac, @PathVariable Integer componentNumber, @RequestBody EndDeviceComponent componentDetails) {
        EndDeviceComponentKey id = new EndDeviceComponentKey(endDeviceMac, componentNumber.shortValue());
        return endDeviceComponentService.updateComponentName(id, componentDetails);
    }
}
