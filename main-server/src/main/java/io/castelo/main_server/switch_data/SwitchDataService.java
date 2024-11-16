package io.castelo.main_server.switch_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SwitchDataService {

    private final SwitchDataRepository switchDataRepository;

    @Autowired
    public SwitchDataService(SwitchDataRepository switchDataRepository) {
        this.switchDataRepository = switchDataRepository;
    }

    public List<SwitchData> findSwitchDataByEndDeviceMac(String endDeviceMac) {
        return switchDataRepository.findByMetaField_EndDeviceMac(endDeviceMac);
    }

    public SwitchData getLatestSwitchDataByEndDeviceMacAndSwitchNumber(String endDeviceMac, int switchNumber) {
        Optional<SwitchData> switchDataDBEntry = switchDataRepository.findFirstByMetaField_EndDeviceMacAndMetaField_SwitchNumber(endDeviceMac, switchNumber);
        return switchDataDBEntry.orElse(null);
    }

    public void insertSwitchData(List<SwitchData> switchData) {
        switchDataRepository.insert(switchData);
    }

    public void insertSwitchData(SwitchData switchData) {
        switchDataRepository.insert(switchData);
    }
}