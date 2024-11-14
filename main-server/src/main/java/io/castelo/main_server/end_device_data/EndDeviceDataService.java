package io.castelo.main_server.end_device_data;

import io.castelo.main_server.sensor_data.Sensor;
import io.castelo.main_server.sensor_data.SensorData;
import io.castelo.main_server.sensor_data.SensorDataService;
import io.castelo.main_server.sensor_data.SensorValue;
import io.castelo.main_server.switch_data.Switch;
import io.castelo.main_server.switch_data.SwitchData;
import io.castelo.main_server.switch_data.SwitchDataService;
import io.castelo.main_server.switch_data.SwitchValue;
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
                            .map(sensorValue -> new SensorData(
                                    endDeviceData.endDeviceMac(),
                                    sensor.sensorNumber(),
                                    sensorValue.timestamp(),
                                    sensorValue.value()
                            ))
                    )
                    .collect(Collectors.toList());
            sensorDataService.saveAllSensorData(sensorData);
        }
        // Process and save switch values
        if (!endDeviceData.switches().isEmpty()) {
            List<SwitchData> switchData = endDeviceData.switches().stream()
                    .flatMap(switchObj -> switchObj.switchValues().stream()
                            .map(switchValue -> new SwitchData(
                                    endDeviceData.endDeviceMac(),
                                    switchObj.switchNumber(),
                                    switchValue.timestamp(),
                                    switchValue.value()
                            ))
                    )
                    .collect(Collectors.toList());
            switchDataService.saveAllSwitchData(switchData);
        }
    }

    public EndDeviceData findByEndDeviceMac(String endDeviceMac) {
        List<SensorData> sensorValues = sensorDataService.findSensorValuesByEndDeviceMac(endDeviceMac);
        List<SwitchData> switchValues = switchDataService.findSwitchValuesByEndDeviceMac(endDeviceMac);

        List<Sensor> sensorDataList = sensorValues.stream()
                .collect(Collectors.groupingBy(
                        SensorData::sensorNumber,
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
                        SwitchData::switchNumber,
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