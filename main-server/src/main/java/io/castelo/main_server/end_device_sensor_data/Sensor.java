package io.castelo.main_server.end_device_sensor_data;

import java.util.List;

public record Sensor(
        int sensorNumber,
        List<SensorValue> sensorValues
) {}
