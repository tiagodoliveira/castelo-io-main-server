package io.castelo.main_server.end_device_data.sensor_data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document
public record SensorValueDBEntry(
        @Id String id,
        @Field("metaField") SensorMetaField sensorMetaField,
        @Field("timestamp") LocalDateTime timestamp,
        @Field("value")String value
) {

    public static SensorValueDBEntry fromSensorValueRequest(SensorValueRequest sensorValueRequest) {
        return new SensorValueDBEntry(null,
                new SensorMetaField(sensorValueRequest.endDeviceMac(), sensorValueRequest.sensorNumber()),
                sensorValueRequest.timestamp(),
                sensorValueRequest.value());
    }
}