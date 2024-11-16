package io.castelo.main_server.end_device_data;

import io.castelo.main_server.sensor_data.*;
import io.castelo.main_server.switch_data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EndDeviceDataService {

    private final SensorDataService sensorDataService;
    private final SwitchDataService switchDataService;

    @Autowired
    public EndDeviceDataService(SensorDataService sensorDataService, SwitchDataService switchDataService) {
        this.sensorDataService = sensorDataService;
        this.switchDataService = switchDataService;
    }

    public void saveEndDeviceData(EndDeviceData endDeviceData) {
        // Process and save sensor values
        if (!endDeviceData.sensors().isEmpty()) {
            List<SensorData> sensorData = endDeviceData.sensors().stream()
                    .flatMap(sensor -> sensor.sensorValues().stream()
                            .map(sensorValue -> new SensorData(null,
                                    new SensorMetaField(endDeviceData.endDeviceMac(), sensor.sensorNumber()),
                                    sensorValue.timestamp(),
                                    sensorValue.value()
                            ))
                    )
                    .collect(Collectors.toList());
            sensorDataService.insertSensorData(sensorData);
        }
        // Process and save switch values
        if (!endDeviceData.switches().isEmpty()) {
            List<SwitchData> switchData = endDeviceData.switches().stream()
                    .flatMap(switchObj -> switchObj.switchValues().stream()
                            .map(switchValue -> new SwitchData(null,
                                    new SwitchMetaField(endDeviceData.endDeviceMac(),
                                    switchObj.switchNumber()),
                                    switchValue.timestamp(),
                                    switchValue.value()
                            ))
                    )
                    .collect(Collectors.toList());
            switchDataService.insertSwitchData(switchData);
        }
    }

    public EndDeviceData findByEndDeviceMac(String endDeviceMac, int maxEntries) {
        List<SensorData> sensorValues = sensorDataService.findSensorDataByEndDeviceMac(endDeviceMac, maxEntries);
        List<SwitchData> switchValues = switchDataService.findSwitchDataByEndDeviceMac(endDeviceMac, maxEntries);

        List<Sensor> sensorDataList = sensorValues.stream()
                .collect(Collectors.groupingBy(
                        sensorDataEntry -> sensorDataEntry.metaField().sensorNumber(),
                        Collectors.mapping(
                                sensorValue -> new SensorValue(sensorValue.timestamp(), sensorValue.value()),
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .map(entry -> new Sensor(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        List<Switch> switchDataList = switchValues.stream()
                .collect(Collectors.groupingBy(
                        switchDataEntry -> switchDataEntry.metaField().switchNumber(),
                        Collectors.mapping(
                                switchValue -> new SwitchValue(switchValue.timestamp(), switchValue.value()),
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .map(entry -> new Switch(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());


        return new EndDeviceData(endDeviceMac, sensorDataList, switchDataList);
    }
}