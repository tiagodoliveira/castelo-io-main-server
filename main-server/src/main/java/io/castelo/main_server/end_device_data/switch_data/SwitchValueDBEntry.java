package io.castelo.main_server.end_device_data.switch_data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document
public record SwitchValueDBEntry(
        @Id String id,
        @Field("metaField") SwitchMetaField switchMetaField,
        @Field("timestamp") LocalDateTime timestamp,
        @Field("value")boolean value
) {

    public static SwitchValueDBEntry fromSwitchValueRequest(SwitchValueRequest switchValueRequest) {
        return new SwitchValueDBEntry(null,
                new SwitchMetaField(switchValueRequest.endDeviceMac(), switchValueRequest.switchNumber()),
                switchValueRequest.timestamp(),
                switchValueRequest.value());
    }
}