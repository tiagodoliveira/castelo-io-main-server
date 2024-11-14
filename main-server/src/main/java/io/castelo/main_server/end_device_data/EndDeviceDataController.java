package io.castelo.main_server.end_device_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/end-device")
public class EndDeviceDataController {

    private final EndDeviceDataService endDeviceDataService;

    @Autowired
    public EndDeviceDataController(EndDeviceDataService endDeviceDataService) {
        this.endDeviceDataService = endDeviceDataService;
    }

    @GetMapping("/{endDeviceMac}")
    @ResponseStatus(HttpStatus.OK)
    public EndDeviceData getEndDeviceDataByMac(@PathVariable String endDeviceMac) {
        return endDeviceDataService.findByEndDeviceMac(endDeviceMac);
    }

    @PostMapping("/save-data")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEndDeviceData(@RequestBody EndDeviceData endDeviceData) {
        endDeviceDataService.saveEndDeviceData(endDeviceData);
    }
}