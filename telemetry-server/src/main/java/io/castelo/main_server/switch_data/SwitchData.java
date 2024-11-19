package io.castelo.main_server.switch_data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "#{T(io.castelo.main_server.database.MongoDBConfig).getSwitchDataCollection()}")
public record SwitchData(
        @Id String id,
        @Field("metaField") SwitchMetaField metaField,
        @Field("timestamp") LocalDateTime timestamp,
        @Field("value")boolean value
) {}