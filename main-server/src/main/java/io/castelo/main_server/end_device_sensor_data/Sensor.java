package io.castelo.main_server.end_device_sensor_data;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

public record Sensor(
        int sensorNumber,
        List<SensorValue> sensorValues
) {
    public static Sensor fromDocument(Document document) {
        int sensorNumber = document.getInteger("sensorNumber");
        List<SensorValue> sensorValues = ((List<Document>) document.get("sensorValues")).stream()
                .map(SensorValue::fromDocument)
                .collect(Collectors.toList());
        return new Sensor(sensorNumber, sensorValues);
    }
}
