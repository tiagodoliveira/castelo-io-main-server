package io.castelo.main_server.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gateways")
public class GatewayController {

    @Autowired
    private GatewayService gatewayService;

    @GetMapping
    public List<Gateway> getAllGateways() {
        return gatewayService.getAllGateways();
    }

    @GetMapping("/{gateway_mac}")
    public ResponseEntity<Gateway> getGateway(@PathVariable String gateway_mac) {
        Gateway gateway = gatewayService.getGateway(gateway_mac);
        return ResponseEntity.ok().body(gateway);
    }

    @PostMapping
    public Gateway createGateway(@RequestBody Gateway gateway) {
        return gatewayService.createGateway(gateway);
    }

    @PutMapping("/{gateway_mac}")
    public ResponseEntity<Gateway> updateGateway(@PathVariable String gateway_mac, @RequestBody Gateway gatewayDetails) {
        return ResponseEntity.ok(gatewayService.updateGateway(gateway_mac, gatewayDetails));
    }

    @DeleteMapping("/{gateway_mac}")
    public ResponseEntity<Void> deleteGateway(@PathVariable String gateway_mac) {
        gatewayService.deleteGateway(gateway_mac);
        return ResponseEntity.noContent().build();
    }
}
