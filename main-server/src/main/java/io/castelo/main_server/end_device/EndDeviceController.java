package io.castelo.main_server.end_device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/end-devices")
public class EndDeviceController {
    @Autowired
    private EndDeviceService endDeviceService;

    @GetMapping
    public List<EndDevice> getAllEndDevices() {
        return endDeviceService.getAllEndDevices();
    }

    @GetMapping("/{end_device_mac}")
    public ResponseEntity<EndDevice> getEndDevice(@PathVariable String end_device_mac) {
        EndDevice endDevice = endDeviceService.getEndDevice(end_device_mac);
        return ResponseEntity.ok().body(endDevice);
    }

    @PostMapping
    public EndDevice createEndDevice(@RequestBody EndDevice endDevice) {
        return endDeviceService.createEndDevice(endDevice);
    }

    @PutMapping("/{end_device_mac}")
    public ResponseEntity<EndDevice> updateEndDevice(@PathVariable String end_device_mac, @RequestBody EndDeviceDTO endDeviceDetails) {
        return ResponseEntity.ok(endDeviceService.updateEndDevice(end_device_mac, endDeviceDetails));
    }

    @DeleteMapping("/{end_device_mac}")
    public ResponseEntity<Void> deleteEndDevice(@PathVariable String end_device_mac) {
        endDeviceService.deleteEndDevice(end_device_mac);
        return ResponseEntity.noContent().build();
    }
}
