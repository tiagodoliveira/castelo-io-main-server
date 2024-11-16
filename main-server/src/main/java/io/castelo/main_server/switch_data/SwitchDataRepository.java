package io.castelo.main_server.switch_data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SwitchDataRepository extends MongoRepository<SwitchData, String> {
    List<SwitchData> findByMetaField_EndDeviceMac(String endDeviceMac);
    List<SwitchData> findByMetaField_EndDeviceMac(String endDeviceMac, PageRequest pageRequest);
    List<SwitchData> findByMetaField_EndDeviceMacAndMetaField_SwitchNumber(String endDeviceMac, int switchNumber);
    List<SwitchData> findByMetaField_EndDeviceMacAndMetaField_SwitchNumber(String endDeviceMac, int switchNumber, PageRequest pageRequest);
    Optional<SwitchData> findFirstByMetaField_EndDeviceMacAndMetaField_SwitchNumber(String endDeviceMac, int switchNumber);
}
