package io.castelo.main_server.database;

import com.influxdb.client.BucketsApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.domain.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfiguration {

    @Value("${influxdb.url}")
    private String influxDBURL;

    @Value("${influxdb.token}")
    private String influxDBToken;

    @Value("${influxdb.org}")
    private String influxDBOrg;

    @Bean
    public InfluxDBClient influxDBClient() {
        InfluxDBClient client = InfluxDBClientFactory.create(influxDBURL, influxDBToken.toCharArray(), influxDBOrg, "sensor_data");

        BucketsApi bucketsApi = client.getBucketsApi();

        // Check if bucket exists and create if it doesn't
        boolean bucketExists = bucketsApi.findBucketByName("sensor_data") != null;
        if (!bucketExists) {
            System.out.println("Creating InfluxDB bucket: sensor_data");
            Bucket bucket = new Bucket();
            bucket.name("sensor_data");
            bucket.orgID(client.getOrganizationsApi().findOrganizations().getFirst().getId());
            bucketsApi.createBucket(bucket);
        } else {
            System.out.println("InfluxDB bucket already exists: sensor_data");
        }
        return client;
    }
}