package io.castelo.main_server.end_device_sensor_data;

import io.castelo.main_server.utils.MACAddressValidator;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record EndDeviceSensorData(
        @Id @Field("endDeviceMac")
        String endDeviceMac,
        @NotEmpty
        List<Sensor> sensors
) {
        public EndDeviceSensorData {
                // Validate and normalize endDeviceMac
                if (endDeviceMac == null || endDeviceMac.isBlank()) {
                        throw new IllegalArgumentException("End Device Mac must not be blank");
                }
                endDeviceMac = MACAddressValidator.normalizeMACAddress(endDeviceMac);
        }

        public static EndDeviceSensorData fromDocuments(List<Document> documents) {
                if (documents.isEmpty()) {
                        return null;
                }
                String endDeviceMac = documents.getFirst().getString("endDeviceMac");

                // Initialize a map to hold aggregated sensor data
                Map<Integer, List<SensorValue>> sensorDataMap = new HashMap<>();

                for (Document document : documents) {
                        int sensorNumber = document.getInteger("sensorNumber");
                        List<SensorValue> sensorValues;

                        Object sensorValuesObj = document.get("sensorValues");
                        if (sensorValuesObj instanceof List<?>) {
                                sensorValues = ((List<?>) sensorValuesObj).stream()
                                        .filter(obj -> obj instanceof Document)
                                        .map(obj -> SensorValue.fromDocument((Document) obj))
                                        .toList();
                        } else {
                                sensorValues = new ArrayList<>();
                        }

                        // Aggregate sensor values by sensorNumber
                        sensorDataMap.computeIfAbsent(sensorNumber, k -> new ArrayList<>()).addAll(sensorValues);
                }
                // Convert the map to a list of sensors
                List<Sensor> sensors = sensorDataMap.entrySet().stream()
                        .map(entry -> new Sensor(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList());

                return new EndDeviceSensorData(endDeviceMac, sensors);
        }
}
