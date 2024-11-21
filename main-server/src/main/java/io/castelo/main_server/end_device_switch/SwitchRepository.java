package io.castelo.main_server.end_device_switch;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SwitchRepository extends JpaRepository<Switch, SwitchKey> {
    List<Switch> findAllByEndDeviceMac(String endDeviceMac);
}
