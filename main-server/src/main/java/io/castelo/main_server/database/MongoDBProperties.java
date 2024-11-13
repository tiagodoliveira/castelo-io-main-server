package io.castelo.main_server.database;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MongoDBProperties {

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
    public static String META_FIELD;
    public static String TIMESTAMP_FIELD;

    @PostConstruct
    private void init() {
        SENSOR_DATA_COLLECTION = this.sensorDataCollection;
        SWITCH_DATA_COLLECTION = this.switchDataCollection;
        META_FIELD = this.metaField;
        TIMESTAMP_FIELD = this.timestampField;
    }
}