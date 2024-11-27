package io.castelo.main_server.end_device_component_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/end-device-telemetry/component-data")
public class ComponentDataController {

    private final ComponentDataService componentDataService;

    @Autowired
    public ComponentDataController(ComponentDataService componentDataService) {
        this.componentDataService = componentDataService;
    }

    @PostMapping("/add-component-value")
    @ResponseStatus(HttpStatus.CREATED)
    public void addComponentValue(@RequestBody EndDeviceComponentData endDeviceComponentData) {
        componentDataService.insertComponentData(endDeviceComponentData);
    }

    @PostMapping("/add-component-values")
    @ResponseStatus(HttpStatus.CREATED)
    public void addComponentValues(@RequestBody List<EndDeviceComponentData> endDeviceComponentDataList) {
        componentDataService.insertComponentData(endDeviceComponentDataList);
    }

    @GetMapping("/{endDeviceMac}/get-all-component-data")
    @ResponseStatus(HttpStatus.OK)
    public List<EndDeviceComponentData> getAllComponentDataByEndDeviceMac(@PathVariable String endDeviceMac) {
        return componentDataService.findAllComponentDataByEndDeviceMac(endDeviceMac);
    }

    @GetMapping("/{endDeviceMac}")
    @ResponseStatus(HttpStatus.OK)
    public List<EndDeviceComponentData> getComponentDataByEndDeviceMac(
            @PathVariable String endDeviceMac,
            @RequestParam(required = false, name = "maxEntries" , defaultValue = "10") Integer maxEntries) {
        return componentDataService.findComponentDataByEndDeviceMac(endDeviceMac, maxEntries);
    }

    @GetMapping("/{endDeviceMac}/{componentNumber}/latest")
    @ResponseStatus(HttpStatus.OK)
    public EndDeviceComponentData getLatestComponentValue(@PathVariable String endDeviceMac, @PathVariable int componentNumber) {
        return componentDataService.getLatestComponentDataByEndDeviceMacAndComponentNumber(endDeviceMac, componentNumber);
    }

    @GetMapping("/{endDeviceMac}/{componentNumber}")
    @ResponseStatus(HttpStatus.OK)
    public List<EndDeviceComponentData> getComponentDataByEndDeviceMacAndComponentNumber(
            @PathVariable String endDeviceMac,
            @PathVariable int componentNumber,
            @RequestParam(required = false, name = "maxEntries", defaultValue = "10") Integer maxEntries) {

        return componentDataService.findComponentDataByEndDeviceMacAndComponentNumber(endDeviceMac, componentNumber, maxEntries);
    }

    @GetMapping("/{endDeviceMac}/{componentNumber}/get-all-component-data")
    @ResponseStatus(HttpStatus.OK)
    public List<EndDeviceComponentData> getAllComponentDataByEndDeviceMacAndComponentNumber(
            @PathVariable String endDeviceMac, @PathVariable int componentNumber) {
        return componentDataService.findAllComponentDataByEndDeviceMacAndComponentNumber(endDeviceMac, componentNumber);
    }

    @GetMapping("/{endDeviceMac}/{componentNumber}/range")
    @ResponseStatus(HttpStatus.OK)
    public List<EndDeviceComponentData> getComponentDataWithinTimeRange(
            @PathVariable String endDeviceMac,
            @PathVariable int componentNumber,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        return componentDataService.getComponentDataWithinTimeRange(endDeviceMac, componentNumber, startDate, endDate);
    }


}