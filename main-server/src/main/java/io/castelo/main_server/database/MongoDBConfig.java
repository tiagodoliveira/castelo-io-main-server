package io.castelo.main_server.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.TimeSeriesOptions;
import jakarta.annotation.PostConstruct;
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

    @Value("${spring.data.mongodb.collections.sensor_data}")
    private String sensorDataCollection;

    @Value("${spring.data.mongodb.collections.switch_data}")
    private String switchDataCollection;

    @Value("${spring.data.mongodb.meta_field}")
    private String metaField;

    @Value("${spring.data.mongodb.timestamp_field}")
    private String timestampField;

    public static String SENSOR_DATA_COLLECTION;
    public static String SWITCH_DATA_COLLECTION;

    @PostConstruct
    private void init() {
        SENSOR_DATA_COLLECTION = this.sensorDataCollection;
        SWITCH_DATA_COLLECTION = this.switchDataCollection;
    }

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

        createTimeseriesCollectionIfNotExists(database, sensorDataCollection);
        createTimeseriesCollectionIfNotExists(database, switchDataCollection);

        return mongoTemplate;
    }

    /**
     * Provides access to the sensor data collection name for use in SpEL expressions.
     *
     * This method is specifically designed to be used within Spring annotations
     * such as @Document(collection = "#{T(io.castelo.main_server.database.MongoDBProperties).getSensorDataCollection()}").
     *
     * @return the sensor data collection name
     */
    @SuppressWarnings("unused")
    public static String getSensorDataCollection() {
        return SENSOR_DATA_COLLECTION;
    }

    @SuppressWarnings("unused")
    public static String getSwitchDataCollection() {
        return SWITCH_DATA_COLLECTION;
    }

    private void createTimeseriesCollectionIfNotExists(MongoDatabase database, String collectionName) {
        if (!database.listCollectionNames().into(new ArrayList<>()).contains(collectionName)) {
            database.createCollection(collectionName, new CreateCollectionOptions()
                    .timeSeriesOptions(new TimeSeriesOptions(timestampField).metaField(metaField)));
            log.info("Timeseries collection created: {}", collectionName);
        } else {
            log.info("Timeseries collection already exists: {}", collectionName);
        }
    }
}