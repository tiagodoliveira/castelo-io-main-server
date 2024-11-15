package io.castelo.main_server.sensor_data;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorDataService {

    @Value("${spring.data.mongodb.collections.sensor_data}")
    private String sensorDataCollectionName;

    private final SensorDataRepository sensorDataRepository;
    private final MongoCollection<Document> sensorDataCollection;

    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository, MongoCollection<Document> sensorDataCollection) {
        this.sensorDataRepository = sensorDataRepository;
        this.sensorDataCollection = sensorDataCollection;
    }

    public List<SensorData> findSensorValuesByEndDeviceMac(String endDeviceMac) {
        List<SensorDataDBEntry> sensorDataDBEntries=  sensorDataRepository.findBySensorMetaField_EndDeviceMac(endDeviceMac);
        return transformDBEntriesInSensorData(sensorDataDBEntries);
    }

    public SensorData getLatestSensorValue(String endDeviceMac, int sensorNumber) {
        Optional<SensorDataDBEntry> sensorDataDBEntry = sensorDataRepository.findFirstBySensorMetaField_EndDeviceMacAndSensorMetaField_SensorNumber(endDeviceMac, sensorNumber);
        return sensorDataDBEntry.map(SensorDataDBEntry::toSensorData).orElse(null);
    }

    public void saveAllSensorData(List<SensorData> sensorData) {
        sensorDataRepository.saveAll(transformSensorDataInDBEntries(sensorData));
    }

    public void saveSensorData(SensorData sensorData) {
        sensorDataRepository.save(SensorDataDBEntry.fromSensorData(sensorData));
    }

    private List<SensorDataDBEntry> transformSensorDataInDBEntries(List<SensorData> sensorValues) {
        return sensorValues.stream().map(SensorDataDBEntry::fromSensorData).toList();
    }

    private List<SensorData> transformDBEntriesInSensorData(List<SensorDataDBEntry> sensorDataEntry) {
        return sensorDataEntry.stream().map(SensorDataDBEntry::toSensorData).toList();
    }
}