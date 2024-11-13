package io.castelo.main_server.end_device_sensor_data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorValueRepository extends MongoRepository<SensorValueDBEntry, String> {
    List<SensorValueDBEntry> findByMetaField_EndDeviceMacAndMetaField_SensorNumber(String endDeviceMac, int sensorNumber);

    SensorValueDBEntry findFirstByOrderByTimestampDesc();

    List<SensorValueDBEntry> findByMetaField_EndDeviceMac(String endDeviceMac);
}
