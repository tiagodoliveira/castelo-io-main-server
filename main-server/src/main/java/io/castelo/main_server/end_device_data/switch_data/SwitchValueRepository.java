package io.castelo.main_server.end_device_data.switch_data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwitchValueRepository extends MongoRepository<SwitchValueDBEntry, String> {
    List<SwitchValueDBEntry> findBySwitchMetaField_EndDeviceMac(String endDeviceMac);
    List<SwitchValueDBEntry> findBySwitchMetaField_EndDeviceMacAndSwitchMetaField_SwitchNumber(String endDeviceMac, int switchNumber);
}
