package io.castelo.main_server.sensor_data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorDataRepository extends MongoRepository<SensorDataDBEntry, String> {
    List<SensorDataDBEntry> findBySensorMetaField_EndDeviceMacAndSensorMetaField_SensorNumber(String endDeviceMac, int sensorNumber);
    Optional<SensorDataDBEntry> findFirstBySensorMetaField_EndDeviceMacAndSensorMetaField_SensorNumber(String endDeviceMac, int sensorNumber );
    List<SensorDataDBEntry> findBySensorMetaField_EndDeviceMac(String endDeviceMac);
}
