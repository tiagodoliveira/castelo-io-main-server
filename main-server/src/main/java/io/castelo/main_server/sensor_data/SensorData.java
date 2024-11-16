package io.castelo.main_server.sensor_data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "#{T(io.castelo.main_server.database.MongoDBConfig).getSensorDataCollection()}")
public record SensorData(
        @Id String id,
        @Field("metaField") SensorMetaField metaField,
        @Field("timestamp") LocalDateTime timestamp,
        @Field("value")String value
) {}