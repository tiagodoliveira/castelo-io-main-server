package io.castelo.main_server.sensor_model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorModelRepository extends JpaRepository<SensorModel, SensorModelKey> {
    List<SensorModel> findAllByModelId(Integer modelId);
}
