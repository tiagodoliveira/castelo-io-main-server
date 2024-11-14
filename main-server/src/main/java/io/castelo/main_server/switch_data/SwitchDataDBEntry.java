package io.castelo.main_server.switch_data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document
public record SwitchDataDBEntry(
        @Id String id,
        @Field("metaField") SwitchMetaField switchMetaField,
        @Field("timestamp") LocalDateTime timestamp,
        @Field("value")boolean value
) {

    public static SwitchDataDBEntry fromSwitchData(SwitchData switchData) {
        return new SwitchDataDBEntry(null,
                new SwitchMetaField(switchData.endDeviceMac(), switchData.switchNumber()),
                switchData.timestamp(),
                switchData.value());
    }

    public static SwitchData toSwitchData(SwitchDataDBEntry switchDataDBEntry) {
        return new SwitchData(switchDataDBEntry.switchMetaField.endDeviceMac(),
                switchDataDBEntry.switchMetaField.switchNumber(),
                switchDataDBEntry.timestamp,
                switchDataDBEntry.value);
    }
}