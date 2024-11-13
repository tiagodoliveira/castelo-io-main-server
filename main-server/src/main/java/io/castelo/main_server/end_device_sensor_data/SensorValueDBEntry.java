package io.castelo.main_server.end_device_sensor_data;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document
public record SensorValueDBEntry(
        @Id String id,
        @Field("metaField") MetaField metaField,
        @Field("timestamp") LocalDateTime timestamp,
        @Field("value")String value
) {

    public static SensorValueDBEntry fromSensorValueRequest(SensorValueRequest sensorValueRequest) {
        return new SensorValueDBEntry(null,
                new MetaField(sensorValueRequest.endDeviceMac(), sensorValueRequest.sensorNumber()),
                sensorValueRequest.timestamp(),
                sensorValueRequest.value());
    }
}