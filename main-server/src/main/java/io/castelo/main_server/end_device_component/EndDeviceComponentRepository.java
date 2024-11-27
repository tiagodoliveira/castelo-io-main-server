package io.castelo.main_server.end_device_component;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndDeviceComponentRepository extends JpaRepository<EndDeviceComponent, EndDeviceComponentKey> {
    List<EndDeviceComponent> findAllByEndDeviceMac(String endDeviceMac);

}
