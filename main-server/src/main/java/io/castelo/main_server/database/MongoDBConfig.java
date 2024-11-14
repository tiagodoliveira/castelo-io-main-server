package io.castelo.main_server.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.TimeSeriesOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    MongoClient mongoClient() {
        return MongoClients.create(mongoHost);
    }

    private static final Logger log = LoggerFactory.getLogger(MongoDBConfig.class);

    @Bean
    public MongoTemplate mongoTemplate() {

        MongoClient mongoClient = MongoClients.create(mongoUri);

        MongoTemplate mongoTemplate = new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient, databaseName));

        MongoDatabase database = mongoTemplate.getDb();

        createTimeseriesCollectionIfNotExists(database, MongoDBProperties.SENSOR_DATA_COLLECTION);
        createTimeseriesCollectionIfNotExists(database, MongoDBProperties.SWITCH_DATA_COLLECTION);

        return mongoTemplate;
    }

    private void createTimeseriesCollectionIfNotExists(MongoDatabase database, String collectionName) {
        if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionName)) {
            database.createCollection(collectionName, new CreateCollectionOptions()
                    .timeSeriesOptions(new TimeSeriesOptions(MongoDBProperties.TIMESTAMP_FIELD).metaField(MongoDBProperties.META_FIELD)));
            log.info("Timeseries collection created: {}", collectionName);
        } else {
            log.info("Timeseries collection already exists: {}", collectionName);
        }
    }
}