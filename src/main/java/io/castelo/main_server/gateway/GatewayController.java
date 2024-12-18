package io.castelo.main_server.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gateways")
public class GatewayController {

    private final GatewayService gatewayService;

    @Autowired
    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping
    public List<Gateway> getAllGateways() {
        return gatewayService.getAllGateways();
    }

    @GetMapping("/{gateway_mac}")
    @ResponseStatus(HttpStatus.OK)
    public Gateway getGateway(@PathVariable String gateway_mac) {
        return gatewayService.getGateway(gateway_mac);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Gateway createGateway(@RequestBody Gateway gateway) {
        return gatewayService.createGateway(gateway);
    }

    @PutMapping("/{gateway_mac}")
    @ResponseStatus(HttpStatus.OK)
    public Gateway updateGateway(@PathVariable String gateway_mac, @RequestBody Gateway gatewayDetails) {
        return gatewayService.updateGateway(gateway_mac, gatewayDetails);
    }

    @DeleteMapping("/{gateway_mac}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGateway(@PathVariable String gateway_mac) {
        gatewayService.deleteGateway(gateway_mac);
    }
}
