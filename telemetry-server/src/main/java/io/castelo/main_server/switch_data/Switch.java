package io.castelo.main_server.switch_data;

import java.util.List;

public record Switch(
        int switchNumber,
        List<SwitchValue> switchValues
) {}
