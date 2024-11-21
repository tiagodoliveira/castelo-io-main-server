package io.castelo.main_server.end_device;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndDeviceRepository extends JpaRepository<EndDevice, String> {
    List<EndDevice> findAllByGateway_GatewayMac(String gatewayMac);
}
