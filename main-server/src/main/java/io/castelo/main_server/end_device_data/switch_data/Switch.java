package io.castelo.main_server.end_device_data.switch_data;

import java.util.List;

public record Switch(
        int switchNumber,
        List<SwitchValue> switchValues
) {}
