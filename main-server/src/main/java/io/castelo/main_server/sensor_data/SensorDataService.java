package io.castelo.main_server.sensor_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;

    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    public List<SensorData> findAllSensorDataByEndDeviceMac(String endDeviceMac) {
        return sensorDataRepository.findByMetaField_EndDeviceMac(endDeviceMac);
    }

    public List<SensorData> findSensorDataByEndDeviceMac(String endDeviceMac, int maxEntries) {
        return sensorDataRepository.findByMetaField_EndDeviceMac(endDeviceMac, PageRequest.of(0, maxEntries));
    }

    public List<SensorData> findSensorDataByEndDeviceMacAndSensorNumber(String endDeviceMac, int sensorNumber, int maxEntries) {
        return sensorDataRepository.findByMetaField_EndDeviceMacAndMetaField_SensorNumber(endDeviceMac, sensorNumber, PageRequest.of(0, maxEntries));
    }

    public List<SensorData> findAllSensorDataByEndDeviceMacAndSensorNumber(String endDeviceMac, int sensorNumber) {
        return sensorDataRepository.findByMetaField_EndDeviceMacAndMetaField_SensorNumber(endDeviceMac, sensorNumber);
    }

    public SensorData getLatestSensorValue(String endDeviceMac, int sensorNumber) {
        Optional<SensorData> sensorData = sensorDataRepository.findFirstByMetaField_EndDeviceMacAndMetaField_SensorNumber(endDeviceMac, sensorNumber);
        return sensorData.orElse(null);
    }

    public void insertSensorData(List<SensorData> sensorData) {
        sensorDataRepository.insert(sensorData);
    }

    public void insertSensorData(SensorData sensorData) {
        sensorDataRepository.insert(sensorData);
    }
}