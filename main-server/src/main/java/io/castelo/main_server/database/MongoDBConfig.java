package io.castelo.main_server.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.TimeSeriesOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.ArrayList;

@Configuration
public class MongoDBConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Bean
    MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        String uri = "mongodb://root:password@localhost:27017/enddevice_data?authSource=admin";

        // Create MongoClient with settings
        MongoClient mongoClient = MongoClients.create(uri);

        MongoTemplate mongoTemplate = new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient, databaseName));

        // Creating timeseries collections if they don't exist
        MongoDatabase database = mongoTemplate.getDb();

        createTimeseriesCollectionIfNotExists(database, MongoDBProperties.SENSOR_DATA_COLLECTION);
        createTimeseriesCollectionIfNotExists(database, MongoDBProperties.SWITCH_DATA_COLLECTION);

        return mongoTemplate;
    }

    private void createTimeseriesCollectionIfNotExists(MongoDatabase database, String collectionName) {
        if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionName)) {
            database.createCollection(collectionName, new CreateCollectionOptions()
                    .timeSeriesOptions(new TimeSeriesOptions(MongoDBProperties.TIMESTAMP_FIELD).metaField(MongoDBProperties.META_FIELD)));
            System.out.println("Timeseries collection created: " + collectionName);
        } else {
            System.out.println("Timeseries collection already exists: " + collectionName);
        }
    }
}