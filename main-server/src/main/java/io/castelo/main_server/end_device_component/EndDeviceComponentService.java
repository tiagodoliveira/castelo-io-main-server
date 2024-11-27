package io.castelo.main_server.end_device_component;

import io.castelo.main_server.end_device_component_model.EndDeviceComponentModel;
import io.castelo.main_server.end_device_component_model.EndDeviceComponentModelService;
import io.castelo.main_server.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EndDeviceComponentService {

    private final EndDeviceComponentRepository endDeviceComponentRepository;
    private final EndDeviceComponentModelService endDeviceComponentModelService;

    @Autowired
    public EndDeviceComponentService(EndDeviceComponentRepository endDeviceComponentRepository,
                                     EndDeviceComponentModelService endDeviceComponentModelService) {
        this.endDeviceComponentRepository = endDeviceComponentRepository;
        this.endDeviceComponentModelService = endDeviceComponentModelService;
    }

    public List<EndDeviceComponent> getAllComponents() {
        return endDeviceComponentRepository.findAll();
    }

    public EndDeviceComponent getComponent(EndDeviceComponentKey id) {
        return endDeviceComponentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Component not found with id: " + id));
    }

    public List<EndDeviceComponent> createComponents(String endDeviceMac, Integer modelId) {
        List<EndDeviceComponentModel> endDeviceComponentModels = endDeviceComponentModelService.getAllComponentModelsByModelId(modelId);
        List<EndDeviceComponent> endDeviceComponents = endDeviceComponentModels.stream().map(componentModel ->
                new EndDeviceComponent(endDeviceMac,
                        componentModel.getComponentNumber(),
                        componentModel.getComponentName(),
                        componentModel.getComponentType())).toList();

        return endDeviceComponentRepository.saveAll(endDeviceComponents);
    }

    public EndDeviceComponent updateComponentName(EndDeviceComponentKey id, EndDeviceComponent componentDetails) {
        EndDeviceComponent endDeviceComponent = endDeviceComponentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Component not found with id: " + id));
        endDeviceComponent.setComponentName(componentDetails.getComponentName());
        return endDeviceComponentRepository.save(endDeviceComponent);
    }
}
