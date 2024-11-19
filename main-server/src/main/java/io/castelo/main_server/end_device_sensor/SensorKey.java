package io.castelo.main_server.end_device_sensor;

public record SensorKey(
        String endDeviceMac,
        Short sensorNumber
) {
}
