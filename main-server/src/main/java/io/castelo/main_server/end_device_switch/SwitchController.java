package io.castelo.main_server.end_device_switch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/switches")
public class SwitchController {

    @Autowired
    private SwitchService switchService;

    @GetMapping
    public List<Switch> getAllSwitches() {
        return switchService.getAllSwitches();
    }

    @GetMapping("/{endDeviceMac}/{switchNumber}")
    public ResponseEntity<Switch> getSwitch(@PathVariable String endDeviceMac, @PathVariable Integer switchNumber) {
        SwitchKey id = new SwitchKey(endDeviceMac, switchNumber.shortValue());
        Switch sw = switchService.getSwitch(id);
        return ResponseEntity.ok().body(sw);
    }

    @PostMapping
    public Switch createSwitch(@RequestBody Switch sw) {
        return switchService.createSwitch(sw);
    }

    @PutMapping("/{endDeviceMac}/{switchNumber}")
    public ResponseEntity<Switch> updateSwitchName(@PathVariable String endDeviceMac, @PathVariable Integer switchNumber, @RequestBody String switchName) {
        SwitchKey id = new SwitchKey(endDeviceMac, switchNumber.shortValue());
        return ResponseEntity.ok(switchService.updateSwitchName(id, switchName));
    }
}
