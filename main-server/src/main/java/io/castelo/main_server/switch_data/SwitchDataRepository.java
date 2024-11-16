package io.castelo.main_server.switch_data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SwitchDataRepository extends MongoRepository<SwitchData, String> {
    List<SwitchData> findByMetaField_EndDeviceMacOrderByTimestampDesc(String endDeviceMac);
    List<SwitchData> findByMetaField_EndDeviceMacOrderByTimestampDesc(String endDeviceMac, PageRequest pageRequest);
    List<SwitchData> findByMetaField_EndDeviceMacAndMetaField_SwitchNumberOrderByTimestampDesc(String endDeviceMac, int switchNumber);
    List<SwitchData> findByMetaField_EndDeviceMacAndMetaField_SwitchNumberOrderByTimestampDesc(String endDeviceMac, int switchNumber, PageRequest pageRequest);
    List<SwitchData> findByMetaField_EndDeviceMacAndMetaField_SwitchNumberAndTimestampIsBetweenOrderByTimestampDesc(String endDeviceMac, int switchNumber, LocalDateTime startDate, LocalDateTime endDate);
    Optional<SwitchData> findFirstByMetaField_EndDeviceMacAndMetaField_SwitchNumberOrderByTimestampDesc(String endDeviceMac, int switchNumber);
}
