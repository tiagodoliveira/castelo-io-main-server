package io.castelo.main_server.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.TimeSeriesGranularity;
import com.mongodb.client.model.TimeSeriesOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
public class MongoDBConfig {

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.host}")
    private String mongodbHost;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongodbHost);
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase(databaseName);
    }

    @PostConstruct
    public void init() {
        MongoDatabase database = mongoClient().getDatabase(databaseName);
        createTimeSeriesCollections(database);
    }

    private void createTimeSeriesCollections(MongoDatabase database) {
        createTimeSeriesCollection(database, MongoDBProperties.SENSOR_DATA_COLLECTION);
        createTimeSeriesCollection(database, MongoDBProperties.SWITCH_DATA_COLLECTION);
    }

    private void createTimeSeriesCollection(MongoDatabase database, String collectionName) {
        try {
            TimeSeriesOptions tsOptions = new TimeSeriesOptions(MongoDBProperties.TIMESTAMP_FIELD)
                    .metaField(MongoDBProperties.META_FIELD)
                    .granularity(TimeSeriesGranularity.SECONDS);
            CreateCollectionOptions collOptions = new CreateCollectionOptions().timeSeriesOptions(tsOptions);

            database.createCollection(collectionName, collOptions);
        } catch (Exception e) {
            // Handle exception, but ignore if the collection already exists
            if (!e.getMessage().contains("already exists")) {
                throw e;
            }
        }
    }
}