package io.castelo.main_server.end_device_switch;

public record SwitchKey(
        String endDeviceMac,
        Short switchNumber
) {
}
