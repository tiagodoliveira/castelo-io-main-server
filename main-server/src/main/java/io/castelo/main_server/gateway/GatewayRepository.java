package io.castelo.main_server.gateway;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GatewayRepository extends JpaRepository<Gateway, String> {
}
