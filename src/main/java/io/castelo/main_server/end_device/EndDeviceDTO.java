package io.castelo.main_server.end_device;

import io.castelo.main_server.gateway.Gateway;

public record EndDeviceDTO(
        String endDeviceIp,
        String endDeviceName,
        Boolean debugMode,
        Gateway gateway,
        String firmware,
        WorkingModes workingMode
) {}