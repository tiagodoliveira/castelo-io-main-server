package io.castelo.main_server.database;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MongoDBProperties {

    @Value("${spring.data.mongodb.meta_field}")
    private String metaField;

    @Value("${spring.data.mongodb.timestamp_field}")
    private String timestampField;

    public static String META_FIELD;
    public static String TIMESTAMP_FIELD;

    @PostConstruct
    private void init() {
        META_FIELD = this.metaField;
        TIMESTAMP_FIELD = this.timestampField;
    }
}