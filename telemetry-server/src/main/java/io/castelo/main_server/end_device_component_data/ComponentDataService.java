package io.castelo.main_server.end_device_component_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComponentDataService {

    private final ComponentDataRepository componentDataRepository;

    @Autowired
    public ComponentDataService(ComponentDataRepository componentDataRepository) {
        this.componentDataRepository = componentDataRepository;
    }

    public void insertComponentData(List<EndDeviceComponentData> endDeviceComponentData) {
        componentDataRepository.insert(endDeviceComponentData);
    }

    public void insertComponentData(EndDeviceComponentData endDeviceComponentData) {
        componentDataRepository.insert(endDeviceComponentData);
    }

    public List<EndDeviceComponentData> findAllComponentDataByEndDeviceMac(String endDeviceMac) {
        return componentDataRepository.findByMetaField_EndDeviceMacOrderByTimestampDesc(endDeviceMac);
    }

    public List<EndDeviceComponentData> findComponentDataByEndDeviceMac(String endDeviceMac, int maxEntries) {
        return componentDataRepository.findByMetaField_EndDeviceMacOrderByTimestampDesc(endDeviceMac, PageRequest.of(0, maxEntries));
    }

    public List<EndDeviceComponentData> findComponentDataByEndDeviceMacAndComponentNumber(String endDeviceMac, int componentNumber, int maxEntries) {
        return componentDataRepository.findByMetaField_EndDeviceMacAndMetaField_ComponentNumberOrderByTimestampDesc(endDeviceMac, componentNumber, PageRequest.of(0, maxEntries));
    }

    public List<EndDeviceComponentData> findAllComponentDataByEndDeviceMacAndComponentNumber(String endDeviceMac, int componentNumber) {
        return componentDataRepository.findByMetaField_EndDeviceMacAndMetaField_ComponentNumberOrderByTimestampDesc(endDeviceMac, componentNumber);
    }

    public EndDeviceComponentData getLatestComponentDataByEndDeviceMacAndComponentNumber(String endDeviceMac, int componentNumber) {
        Optional<EndDeviceComponentData> componentData = componentDataRepository.findFirstByMetaField_EndDeviceMacAndMetaField_ComponentNumberOrderByTimestampDesc(endDeviceMac, componentNumber);
        return componentData.orElse(null);
    }

    public List<EndDeviceComponentData> getComponentDataWithinTimeRange(String endDeviceMac, int componentNumber, LocalDateTime startDate, LocalDateTime endDate) {
        return componentDataRepository.findByMetaField_EndDeviceMacAndMetaField_ComponentNumberAndTimestampBetweenOrderByTimestampDesc(
                endDeviceMac, componentNumber, startDate.minusNanos(1), endDate.plusNanos(1)); // plus and minus nanos ensures the dates are inclusive
    }
}