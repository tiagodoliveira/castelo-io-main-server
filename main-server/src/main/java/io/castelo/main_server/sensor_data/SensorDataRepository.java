package io.castelo.main_server.sensor_data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SensorDataRepository extends MongoRepository<SensorData, String> {
    List<SensorData> findByMetaField_EndDeviceMacOrderByTimestampDesc(String endDeviceMac);
    List<SensorData> findByMetaField_EndDeviceMacOrderByTimestampDesc(String endDeviceMac, PageRequest pageRequest);
    List<SensorData> findByMetaField_EndDeviceMacAndMetaField_SensorNumberOrderByTimestampDesc(String endDeviceMac, int sensorNumber);
    List<SensorData> findByMetaField_EndDeviceMacAndMetaField_SensorNumberOrderByTimestampDesc(String endDeviceMac, int sensorNumber, PageRequest pageRequest);
    List<SensorData> findByMetaField_EndDeviceMacAndMetaField_SensorNumberAndTimestampBetweenOrderByTimestampDesc(String endDeviceMac, int sensorNumber, LocalDateTime startDate, LocalDateTime endDate);
    Optional<SensorData> findFirstByMetaField_EndDeviceMacAndMetaField_SensorNumberOrderByTimestampDesc(String endDeviceMac, int sensorNumber );



}
