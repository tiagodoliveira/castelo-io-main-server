package io.castelo.main_server.end_device_component_data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComponentDataRepository extends MongoRepository<EndDeviceComponentData, String> {
    List<EndDeviceComponentData> findByMetaField_EndDeviceMacOrderByTimestampDesc(String endDeviceMac);
    List<EndDeviceComponentData> findByMetaField_EndDeviceMacOrderByTimestampDesc(String endDeviceMac, PageRequest pageRequest);
    List<EndDeviceComponentData> findByMetaField_EndDeviceMacAndMetaField_ComponentNumberOrderByTimestampDesc(String endDeviceMac, int componentNumber);
    List<EndDeviceComponentData> findByMetaField_EndDeviceMacAndMetaField_ComponentNumberOrderByTimestampDesc(String endDeviceMac, int componentNumber, PageRequest pageRequest);
    List<EndDeviceComponentData> findByMetaField_EndDeviceMacAndMetaField_ComponentNumberAndTimestampBetweenOrderByTimestampDesc(String endDeviceMac, int componentNumber, LocalDateTime startDate, LocalDateTime endDate);
    Optional<EndDeviceComponentData> findFirstByMetaField_EndDeviceMacAndMetaField_ComponentNumberOrderByTimestampDesc(String endDeviceMac, int componentNumber );
}
