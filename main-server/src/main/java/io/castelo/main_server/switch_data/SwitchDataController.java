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
    public void addSwitchData(@RequestBody SwitchData switchData) {
        switchDataService.insertSwitchData(switchData);
    }

    @PostMapping("/add-switch-values")
    @ResponseStatus(HttpStatus.CREATED)
    public void addMultipleSwitchData(@RequestBody List<SwitchData> switchDataList) {
        switchDataService.insertSwitchData(switchDataList);
    }

    @GetMapping("/{endDeviceMac}")
    @ResponseStatus(HttpStatus.OK)
    public List<SwitchData> getSwitchDataByEndDeviceMac(@PathVariable String endDeviceMac) {
        return switchDataService.findSwitchDataByEndDeviceMac(endDeviceMac);
    }

    @GetMapping("/{endDeviceMac}/{switchNumber}/latest")
    @ResponseStatus(HttpStatus.OK)
    public SwitchData getLatestSwitchData(@PathVariable String endDeviceMac, @PathVariable int switchNumber) {
        return switchDataService.getLatestSwitchDataByEndDeviceMacAndSwitchNumber(endDeviceMac, switchNumber);
    }
}