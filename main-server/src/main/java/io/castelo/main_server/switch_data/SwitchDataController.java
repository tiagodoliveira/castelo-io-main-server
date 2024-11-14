package io.castelo.main_server.switch_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/end-device/switch-data")
public class SwitchDataController {

    private final SwitchDataService switchDataService;

    @Autowired
    public SwitchDataController(SwitchDataService switchDataService) {
        this.switchDataService = switchDataService;
    }

    @PostMapping("/add-switch-value")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSwitchValue(@RequestBody SwitchData switchData) {
        switchDataService.saveSwitchValue(switchData);
    }

    @GetMapping("/{endDeviceMac}")
    @ResponseStatus(HttpStatus.OK)
    public List<SwitchData> getSwitchDataByEndDeviceMac(@PathVariable String endDeviceMac) {
        return switchDataService.findSwitchValuesByEndDeviceMac(endDeviceMac);
    }

    @GetMapping("/{endDeviceMac}/{switchNumber}/latest")
    @ResponseStatus(HttpStatus.OK)
    public SwitchData getLatestSwitchValue(@PathVariable String endDeviceMac, @PathVariable int switchNumber) {
        return switchDataService.getLatestSwitchValue(endDeviceMac, switchNumber);
    }
}