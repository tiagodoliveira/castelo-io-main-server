package io.castelo.main_server.sensor_data;

import org.springframework.data.mongodb.core.mapping.Field;

public record SensorMetaField(
        @Field("endDeviceMac")
        String endDeviceMac,
        @Field("sensorNumber")
        int sensorNumber
) {
}
