package io.castelo.main_server.sensor_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorDataService {

    @Value("${spring.data.mongodb.collections.sensor_data}")
    private String sensorDataCollection;

    private final SensorDataRepository sensorDataRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository, MongoTemplate mongoTemplate) {
        this.sensorDataRepository = sensorDataRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public List<SensorData> findSensorValuesByEndDeviceMac(String endDeviceMac) {
        List<SensorDataDBEntry> sensorDataDBEntries=  sensorDataRepository.findBySensorMetaField_EndDeviceMac(endDeviceMac);
        return transformDBEntriesInSensorData(sensorDataDBEntries);
    }

    public SensorDataDBEntry getLatestEntry() {
        return sensorDataRepository.findFirstByOrderByTimestampDesc();
    }

    public void saveAllSensorData(List<SensorData> sensorData) {
        sensorDataRepository.saveAll(transformSensorDataInDBEntries(sensorData));
    }

    public void saveSensorData(SensorData sensorData) {
        mongoTemplate.save(SensorDataDBEntry.fromSensorData(sensorData), sensorDataCollection);
    }

    private List<SensorDataDBEntry> transformSensorDataInDBEntries(List<SensorData> sensorValues) {
        return sensorValues.stream().map(SensorDataDBEntry::fromSensorData).toList();
    }

    private List<SensorData> transformDBEntriesInSensorData(List<SensorDataDBEntry> sensorDataEntry) {
        return sensorDataEntry.stream().map(SensorDataDBEntry::toSensorData).toList();
    }
}