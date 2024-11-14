package io.castelo.main_server.switch_data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SwitchDataRepository extends MongoRepository<SwitchDataDBEntry, String> {
    List<SwitchDataDBEntry> findBySwitchMetaField_EndDeviceMac(String endDeviceMac);
    List<SwitchDataDBEntry> findBySwitchMetaField_EndDeviceMacAndSwitchMetaField_SwitchNumber(String endDeviceMac, int switchNumber);
    Optional<SwitchDataDBEntry> findFirstBySwitchMetaField_EndDeviceMacAndSwitchMetaField_SwitchNumber(String endDeviceMac, int switchNumber);
}
