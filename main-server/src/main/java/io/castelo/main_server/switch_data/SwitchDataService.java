package io.castelo.main_server.switch_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SwitchDataService {

    private final SwitchDataRepository switchDataRepository;

    @Autowired
    public SwitchDataService(SwitchDataRepository switchDataRepository) {
        this.switchDataRepository = switchDataRepository;
    }

    public void insertSwitchData(List<SwitchData> switchData) {
        switchDataRepository.insert(switchData);
    }

    public void insertSwitchData(SwitchData switchData) {
        switchDataRepository.insert(switchData);
    }

    public List<SwitchData> findSwitchDataByEndDeviceMac(String endDeviceMac, int maxEntries) {
        return switchDataRepository.findByMetaField_EndDeviceMacOrderByTimestampDesc(endDeviceMac, PageRequest.of(0, maxEntries));
    }

    public List<SwitchData> findSwitchDataByEndDeviceMacAndSwitchNumber(String endDeviceMac, int switchNumber, int maxEntries) {
        return switchDataRepository.findByMetaField_EndDeviceMacAndMetaField_SwitchNumberOrderByTimestampDesc(endDeviceMac, switchNumber, PageRequest.of(0, maxEntries));
    }

    public List<SwitchData> findAllSwitchDataByEndDeviceMac(String endDeviceMac) {
        return switchDataRepository.findByMetaField_EndDeviceMacOrderByTimestampDesc(endDeviceMac);
    }

    public List<SwitchData> findAllSwitchDataByEndDeviceMacAndSwitchNumber(String endDeviceMac, int switchNumber) {
        return switchDataRepository.findByMetaField_EndDeviceMacAndMetaField_SwitchNumberOrderByTimestampDesc(endDeviceMac, switchNumber);
    }

    public SwitchData getLatestSwitchDataByEndDeviceMacAndSwitchNumber(String endDeviceMac, int switchNumber) {
        Optional<SwitchData> switchDataDBEntry = switchDataRepository.findFirstByMetaField_EndDeviceMacAndMetaField_SwitchNumberOrderByTimestampDesc(endDeviceMac, switchNumber);
        return switchDataDBEntry.orElse(null);
    }

    public List<SwitchData> getSwitchDataWithinTimeRange(String endDeviceMac, int switchNumber, LocalDateTime startDate, LocalDateTime endDate) {
        return switchDataRepository.findByMetaField_EndDeviceMacAndMetaField_SwitchNumberAndTimestampIsBetweenOrderByTimestampDesc(
                endDeviceMac, switchNumber, startDate.minusNanos(1), endDate.plusNanos(1)); // plus and minus nanos ensures the dates are inclusive
    }
}