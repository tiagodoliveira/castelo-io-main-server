package io.castelo.main_server.end_device_component_model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndDeviceComponentModelRepository extends JpaRepository<EndDeviceComponentModel, EndDeviceComponentModelKey> {
    List<EndDeviceComponentModel> findAllByModelId(Integer modelId);
}
