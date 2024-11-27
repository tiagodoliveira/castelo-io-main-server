package io.castelo.main_server.end_device_component_data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "#{T(io.castelo.main_server.database.MongoDBConfig).getComponentDataCollection()}")
public record EndDeviceComponentData(
        @Id String id,
        @Field("metaField") ComponentMetaField metaField,
        @Field("timestamp") LocalDateTime timestamp,
        @Field("value")String value
) {}