package io.castelo.main_server.end_device_sensor_data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwitchValueRepository extends MongoRepository<SwitchValueDBEntry, String> {
    List<SwitchValueDBEntry> findByEndDeviceMac(String endDeviceMac);
}
