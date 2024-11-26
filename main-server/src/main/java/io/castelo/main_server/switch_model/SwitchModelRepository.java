package io.castelo.main_server.switch_model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SwitchModelRepository extends JpaRepository<SwitchModel, SwitchModelKey> {
    List<SwitchModel> findAllByModelId(Integer modelId);
}
