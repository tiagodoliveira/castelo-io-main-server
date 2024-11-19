package io.castelo.main_server.end_device_switch;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SwitchRepository extends JpaRepository<Switch, SwitchKey> {
}
