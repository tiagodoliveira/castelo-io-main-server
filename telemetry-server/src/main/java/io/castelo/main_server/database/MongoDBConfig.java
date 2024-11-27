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

    @Value("${spring.data.mongodb.collections.component_data}")
    private String componentDataCollection;

    @Value("${spring.data.mongodb.meta_field}")
    private String metaField;

    @Value("${spring.data.mongodb.timestamp_field}")
    private String timestampField;

    public static String COMPONENT_DATA_COLLECTION;

    @PostConstruct
    private void init() {
        COMPONENT_DATA_COLLECTION = this.componentDataCollection;
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

        createTimeseriesCollectionIfNotExists(database, componentDataCollection);

        return mongoTemplate;
    }

    /**
     * Provides access to the component data collection name for use in SpEL expressions.
     *
     * This method is specifically designed to be used within Spring annotations
     * such as @Document(collection = "#{T(io.castelo.main_server.database.MongoDBProperties).getComponentDataCollection()}").
     *
     * @return the component data collection name
     */
    @SuppressWarnings("unused")
    public static String getComponentDataCollection() {
        return COMPONENT_DATA_COLLECTION;
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