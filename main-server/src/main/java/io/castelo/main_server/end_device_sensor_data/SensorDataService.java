package io.castelo.main_server.end_device_sensor_data;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

@Service
public class SensorDataService {

    @Autowired
    private MongoDatabase mongoDatabase;

    public void addSensorData(EndDeviceSensorData endDeviceSensorData) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("timeseries_sensor_data");

        String endDeviceMac = endDeviceSensorData.endDeviceMac();
        for (Sensor sensor : endDeviceSensorData.sensors()) {
            List<Document> sensorValueDocs = sensor.sensorValues().stream()
                    .map(sensorValue -> new Document("timestamp", sensorValue.timestamp())
                            .append("value", sensorValue.value()))
                    .collect(Collectors.toList());

            Document sensorDocument = new Document("endDeviceMac", endDeviceMac)
                    .append("sensorNumber", sensor.sensorNumber())
                    .append("sensorValues", sensorValueDocs);

            collection.updateOne(
                    eq("endDeviceMac", endDeviceMac),
                    new Document("$push", new Document("sensors", sensorDocument)),
                    new com.mongodb.client.model.UpdateOptions().upsert(true)
            );
        }
    }

    // Optimized example of querying and returning an EndDeviceSensorData instance
    public Optional<EndDeviceSensorData> getSensorDataByMac(String endDeviceMac) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("timeseries_sensor_data");
        List<Document> documents = collection.find(eq("endDeviceMac", endDeviceMac)).into(new ArrayList<>());
        return documents.isEmpty() ? Optional.empty() : Optional.ofNullable(EndDeviceSensorData.fromDocuments(documents));
    }
}