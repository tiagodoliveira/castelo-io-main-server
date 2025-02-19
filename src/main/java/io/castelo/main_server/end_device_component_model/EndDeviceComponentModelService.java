package io.castelo.main_server.end_device_component_model;

import io.castelo.main_server.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EndDeviceComponentModelService {

    private final EndDeviceComponentModelRepository endDeviceComponentModelRepository;

    @Autowired
    public EndDeviceComponentModelService(EndDeviceComponentModelRepository endDeviceComponentModelRepository) {
        this.endDeviceComponentModelRepository = endDeviceComponentModelRepository;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<EndDeviceComponentModel> getAllComponentModels() {
        return endDeviceComponentModelRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<EndDeviceComponentModel> createComponents(List<EndDeviceComponentModel> endDeviceComponentModel) {
        return endDeviceComponentModelRepository.saveAll(endDeviceComponentModel);
    }

    public List<EndDeviceComponentModel> getComponentModel(Integer modelId) {
        List<EndDeviceComponentModel> endDeviceComponentModels = endDeviceComponentModelRepository.findAllByModelId(modelId);
        if (endDeviceComponentModels.isEmpty())
            throw new ResourceNotFoundException("Component Model not found with model id: " + modelId);
        return endDeviceComponentModels;
    }
}
