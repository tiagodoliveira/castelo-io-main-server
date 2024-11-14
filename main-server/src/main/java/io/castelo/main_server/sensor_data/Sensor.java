package io.castelo.main_server.sensor_data;

import java.util.List;

public record Sensor(
        int sensorNumber,
        List<SensorValue> sensorValues
) {}
