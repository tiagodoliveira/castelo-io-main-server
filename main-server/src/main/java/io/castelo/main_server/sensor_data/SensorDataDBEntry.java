package io.castelo.main_server.sensor_data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document
public record SensorDataDBEntry(
        @Id String id,
        @Field("metaField") SensorMetaField sensorMetaField,
        @Field("timestamp") LocalDateTime timestamp,
        @Field("value")String value
) {

    public static SensorDataDBEntry fromSensorData(SensorData sensorData) {
        return new SensorDataDBEntry(null,
                new SensorMetaField(sensorData.endDeviceMac(), sensorData.sensorNumber()),
                sensorData.timestamp(),
                sensorData.value());
    }

    public static SensorData toSensorData(SensorDataDBEntry sensorDataDBEntry) {
        return new SensorData(sensorDataDBEntry.sensorMetaField.endDeviceMac(),
                sensorDataDBEntry.sensorMetaField.sensorNumber(),
                sensorDataDBEntry.timestamp,
                sensorDataDBEntry.value);
    }
}