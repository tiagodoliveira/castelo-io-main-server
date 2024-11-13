package io.castelo.main_server.end_device_sensor_data;

import org.springframework.data.mongodb.core.mapping.Field;

public record MetaField(
        @Field("endDeviceMac")
        String endDeviceMac,
        @Field("sensorNumber")
        int sensorNumber
) {
}
