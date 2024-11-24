package io.castelo.main_server.end_device;

import io.castelo.main_server.end_device_model.EndDeviceModel;
import io.castelo.main_server.gateway.Gateway;

public record EndDeviceDTO(
        String endDeviceIp,
        EndDeviceModel endDeviceModel,
        String endDeviceName,
        Boolean debugMode,
        Gateway gateway,
        String firmware,
        DeviceMode workingMode
) {}