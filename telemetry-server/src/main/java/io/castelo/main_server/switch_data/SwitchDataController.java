package io.castelo.main_server.switch_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/end-device-telemetry/switch-data")
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

    @GetMapping("/{endDeviceMac}/get-all-switch-data")
    @ResponseStatus(HttpStatus.OK)
    public List<SwitchData> getAllSwitchDataByEndDeviceMac(@PathVariable String endDeviceMac) {
        return switchDataService.findAllSwitchDataByEndDeviceMac(endDeviceMac);
    }

    @GetMapping("/{endDeviceMac}")
    @ResponseStatus(HttpStatus.OK)
    public List<SwitchData> getSwitchDataByEndDeviceMac(
            @PathVariable String endDeviceMac,
            @RequestParam(required = false, name = "maxEntries", defaultValue = "10") Integer maxEntries) {
        return switchDataService.findSwitchDataByEndDeviceMac(endDeviceMac, maxEntries);
    }

    @GetMapping("/{endDeviceMac}/{switchNumber}/latest")
    @ResponseStatus(HttpStatus.OK)
    public SwitchData getLatestSwitchData(@PathVariable String endDeviceMac, @PathVariable int switchNumber) {
        return switchDataService.getLatestSwitchDataByEndDeviceMacAndSwitchNumber(endDeviceMac, switchNumber);
    }

    @GetMapping("/{endDeviceMac}/{switchNumber}")
    @ResponseStatus(HttpStatus.OK)
    public List<SwitchData> getSwitchDataByEndDeviceMacAndSwitchNumber(
            @PathVariable String endDeviceMac,
            @PathVariable int switchNumber,
            @RequestParam(required = false, name = "maxEntries", defaultValue = "10") Integer maxEntries)
    {
        return switchDataService.findSwitchDataByEndDeviceMacAndSwitchNumber(endDeviceMac, switchNumber, maxEntries);
    }

    @GetMapping("/{endDeviceMac}/{switchNumber}/get-all-switch-data")
    @ResponseStatus(HttpStatus.OK)
    public List<SwitchData> getAllSwitchDataByEndDeviceMacAndSwitchNumber(
            @PathVariable String endDeviceMac, @PathVariable int switchNumber) {
        return switchDataService.findAllSwitchDataByEndDeviceMacAndSwitchNumber(endDeviceMac, switchNumber);
    }

    @GetMapping("/{endDeviceMac}/{switchNumber}/range")
    @ResponseStatus(HttpStatus.OK)
    public List<SwitchData> getSwitchDataWithinTimeRange(
            @PathVariable String endDeviceMac,
            @PathVariable int switchNumber,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        return switchDataService.getSwitchDataWithinTimeRange(endDeviceMac, switchNumber, startDate, endDate);
    }
}