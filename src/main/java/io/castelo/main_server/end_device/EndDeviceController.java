package io.castelo.main_server.end_device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/end-devices")
public class EndDeviceController {

    private final EndDeviceService endDeviceService;

    @Autowired
    public EndDeviceController(EndDeviceService endDeviceService) {
        this.endDeviceService = endDeviceService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EndDevice> getAllEndDevices() {
        return endDeviceService.getAllEndDevices();
    }

    @GetMapping("/{endDeviceMac}")
    @ResponseStatus(HttpStatus.OK)
    public EndDevice getEndDevice(@PathVariable String endDeviceMac) {
        return endDeviceService.getEndDevice(endDeviceMac);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EndDevice createEndDevice(@RequestBody EndDevice endDevice) {
        return endDeviceService.createEndDevice(endDevice);
    }

    @PutMapping("/{endDeviceMac}")
    @ResponseStatus(HttpStatus.OK)
    public EndDevice updateEndDevice(@PathVariable String endDeviceMac, @RequestBody EndDeviceDTO endDeviceDetails) {
        return endDeviceService.updateEndDevice(endDeviceMac, endDeviceDetails);
    }

    @DeleteMapping("/{endDeviceMac}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEndDevice(@PathVariable String endDeviceMac) {
        endDeviceService.deleteEndDevice(endDeviceMac);
    }
}
