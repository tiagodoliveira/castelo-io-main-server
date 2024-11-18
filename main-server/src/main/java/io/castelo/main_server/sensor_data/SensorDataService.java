package io.castelo.main_server.sensor_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;

    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    public void insertSensorData(List<SensorData> sensorData) {
        sensorDataRepository.insert(sensorData);
    }

    public void insertSensorData(SensorData sensorData) {
        sensorDataRepository.insert(sensorData);
    }

    public List<SensorData> findAllSensorDataByEndDeviceMac(String endDeviceMac) {
        return sensorDataRepository.findByMetaField_EndDeviceMacOrderByTimestampDesc(endDeviceMac);
    }

    public List<SensorData> findSensorDataByEndDeviceMac(String endDeviceMac, int maxEntries) {
        return sensorDataRepository.findByMetaField_EndDeviceMacOrderByTimestampDesc(endDeviceMac, PageRequest.of(0, maxEntries));
    }

    public List<SensorData> findSensorDataByEndDeviceMacAndSensorNumber(String endDeviceMac, int sensorNumber, int maxEntries) {
        return sensorDataRepository.findByMetaField_EndDeviceMacAndMetaField_SensorNumberOrderByTimestampDesc(endDeviceMac, sensorNumber, PageRequest.of(0, maxEntries));
    }

    public List<SensorData> findAllSensorDataByEndDeviceMacAndSensorNumber(String endDeviceMac, int sensorNumber) {
        return sensorDataRepository.findByMetaField_EndDeviceMacAndMetaField_SensorNumberOrderByTimestampDesc(endDeviceMac, sensorNumber);
    }

    public SensorData getLatestSensorDataByEndDeviceMacAndSensorNumber(String endDeviceMac, int sensorNumber) {
        Optional<SensorData> sensorData = sensorDataRepository.findFirstByMetaField_EndDeviceMacAndMetaField_SensorNumberOrderByTimestampDesc(endDeviceMac, sensorNumber);
        return sensorData.orElse(null);
    }

    public List<SensorData> getSensorDataWithinTimeRange(String endDeviceMac, int sensorNumber, LocalDateTime startDate, LocalDateTime endDate) {
        return sensorDataRepository.findByMetaField_EndDeviceMacAndMetaField_SensorNumberAndTimestampBetweenOrderByTimestampDesc(
                endDeviceMac, sensorNumber, startDate.minusNanos(1), endDate.plusNanos(1)); // plus and minus nanos ensures the dates are inclusive
    }
}