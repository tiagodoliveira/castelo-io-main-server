package io.castelo.main_server.end_device_data;

import io.castelo.main_server.end_device_component_data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EndDeviceDataService {

    private final ComponentDataService componentDataService;

    @Autowired
    public EndDeviceDataService(ComponentDataService componentDataService) {
        this.componentDataService = componentDataService;
    }

    public void saveEndDeviceData(EndDeviceData endDeviceData) {
        if (!endDeviceData.endDeviceComponents().isEmpty()) {
            List<EndDeviceComponentData> endDeviceComponentData = endDeviceData.endDeviceComponents().stream()
                    .flatMap(endDeviceComponent -> endDeviceComponent.componentValues().stream()
                            .map(componentValue -> new EndDeviceComponentData(null,
                                    new ComponentMetaField(endDeviceData.endDeviceMac(), endDeviceComponent.componentNumber()),
                                    componentValue.timestamp(),
                                    componentValue.value()
                            ))
                    )
                    .collect(Collectors.toList());
            componentDataService.insertComponentData(endDeviceComponentData);
        }
    }

    public EndDeviceData findByEndDeviceMac(String endDeviceMac, int maxEntries) {
        List<EndDeviceComponentData> componentValues = componentDataService.findComponentDataByEndDeviceMac(endDeviceMac, maxEntries);
        return getEndDeviceData(endDeviceMac, componentValues);
    }

    public EndDeviceData findAllByEndDeviceMac(String endDeviceMac){
        List<EndDeviceComponentData> componentValues = componentDataService.findAllComponentDataByEndDeviceMac(endDeviceMac);
        return getEndDeviceData(endDeviceMac, componentValues);
    }

    private EndDeviceData getEndDeviceData(String endDeviceMac, List<EndDeviceComponentData> componentValues) {
        List<EndDeviceComponent> endDeviceComponentDataList = componentValues.stream()
                .collect(Collectors.groupingBy(
                        endDeviceComponentDataEntry -> endDeviceComponentDataEntry.metaField().componentNumber(),
                        Collectors.mapping(
                                componentValue -> new ComponentValue(componentValue.timestamp(), componentValue.value()),
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .map(entry -> new EndDeviceComponent(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new EndDeviceData(endDeviceMac, endDeviceComponentDataList);
    }
}