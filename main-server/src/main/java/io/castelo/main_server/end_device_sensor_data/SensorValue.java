package io.castelo.main_server.end_device_sensor_data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import org.bson.Document;

public record SensorValue(LocalDateTime timestamp, String value) {

    public static SensorValue fromDocument(Document document) {
        LocalDateTime timestamp = document.getDate("timestamp").toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        String value = document.getString("value");
        return new SensorValue(timestamp, value);
    }
}
