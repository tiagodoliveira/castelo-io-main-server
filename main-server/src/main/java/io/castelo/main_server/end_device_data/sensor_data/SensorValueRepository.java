package io.castelo.main_server.end_device_data.sensor_data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorValueRepository extends MongoRepository<SensorValueDBEntry, String> {
    List<SensorValueDBEntry> findBySensorMetaField_EndDeviceMacAndSensorMetaField_SensorNumber(String endDeviceMac, int sensorNumber);

    SensorValueDBEntry findFirstByOrderByTimestampDesc();

    List<SensorValueDBEntry> findBySensorMetaField_EndDeviceMac(String endDeviceMac);
}
