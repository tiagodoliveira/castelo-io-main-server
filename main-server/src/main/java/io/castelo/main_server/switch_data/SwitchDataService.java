package io.castelo.main_server.switch_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SwitchDataService {

    @Value("${spring.data.mongodb.collections.switch_data}")
    private String switchDataCollection;

    private final SwitchDataRepository switchDataRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public SwitchDataService(SwitchDataRepository switchDataRepository, MongoTemplate mongoTemplate) {
        this.switchDataRepository = switchDataRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public List<SwitchData> findSwitchValuesByEndDeviceMac(String endDeviceMac) {
        List<SwitchDataDBEntry> switchDataDBEntries = switchDataRepository.findBySwitchMetaField_EndDeviceMac(endDeviceMac);
        return transformDBEntriesInSwitchData(switchDataDBEntries);
    }

    public SwitchData getLatestSwitchValue(String endDeviceMac, int switchNumber) {
        Optional<SwitchDataDBEntry> switchDataDBEntry = switchDataRepository.findFirstBySwitchMetaField_EndDeviceMacAndSwitchMetaField_SwitchNumber(endDeviceMac, switchNumber);
        return switchDataDBEntry.map(SwitchDataDBEntry::toSwitchData).orElse(null);
    }

    public void saveSwitchValues(List<SwitchDataDBEntry> switchValues) {
        switchDataRepository.saveAll(switchValues);
    }

    public void saveSwitchValue(SwitchData switchValue) {
        mongoTemplate.save(switchValue, switchDataCollection);
    }

    public void saveAllSwitchData(List<SwitchData> switchData) {

    }

    private List<SwitchDataDBEntry> transformSwitchDataInDBEntries(List<SwitchData> switchData) {
        return switchData.stream().map(SwitchDataDBEntry::fromSwitchData).toList();
    }

    private List<SwitchData> transformDBEntriesInSwitchData(List<SwitchDataDBEntry> switchDataEntry) {
        return switchDataEntry.stream().map(SwitchDataDBEntry::toSwitchData).toList();
    }
}