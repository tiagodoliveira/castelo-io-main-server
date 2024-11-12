package io.castelo.main_server.end_device_sensor_data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataRepository extends MongoRepository<EndDeviceSensorData, String> {

}
