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

    @GetMapping("/{end_device_mac}")
    @ResponseStatus(HttpStatus.OK)
    public EndDevice getEndDevice(@PathVariable String end_device_mac) {
        return endDeviceService.getEndDevice(end_device_mac);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EndDevice createEndDevice(@RequestBody EndDevice endDevice) {
        return endDeviceService.createEndDevice(endDevice);
    }

    @PutMapping("/{end_device_mac}")
    @ResponseStatus(HttpStatus.OK)
    public EndDevice updateEndDevice(@PathVariable String end_device_mac, @RequestBody EndDeviceDTO endDeviceDetails) {
        return endDeviceService.updateEndDevice(end_device_mac, endDeviceDetails);
    }

    @DeleteMapping("/{end_device_mac}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEndDevice(@PathVariable String end_device_mac) {
        endDeviceService.deleteEndDevice(end_device_mac);
    }
}
