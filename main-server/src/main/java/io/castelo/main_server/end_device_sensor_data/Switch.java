package io.castelo.main_server.end_device_sensor_data;

import java.util.List;

public record Switch(
        int switchNumber,
        List<SwitchValue> switchValues
) {}
