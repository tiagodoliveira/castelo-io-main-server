package io.castelo.main_server.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.List;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.BucketsApi;
import com.influxdb.client.domain.Bucket;

@Configuration
public class InfluxDBConfiguration {

    @Value("${influxdb.url}")
    private String influxDBURL;

    @Value("${influxdb.token}")
    private String influxDBToken;

    @Value("${influxdb.org}")
    private String influxDBOrg;

    @Value("${influxdb.buckets}")
    private String influxDBBuckets;

    @Bean
    public InfluxDBClient influxDBClient() {
        InfluxDBClient client = InfluxDBClientFactory.create(influxDBURL, influxDBToken.toCharArray(), influxDBOrg);

        // Read bucket names from properties and create them
        List<String> bucketNames = Arrays.asList(influxDBBuckets.split(","));
        bucketNames.forEach(bucketName -> ensureBucketExists(client, bucketName));

        return client;
    }

    private void ensureBucketExists(InfluxDBClient client, String bucketName) {
        BucketsApi bucketsApi = client.getBucketsApi();
        boolean bucketExists = bucketsApi.findBucketByName(bucketName) != null;
        if (!bucketExists) {
            System.out.println("Creating InfluxDB bucket: " + bucketName);
            Bucket bucket = new Bucket();
            bucket.name(bucketName);
            bucket.orgID(client.getOrganizationsApi().findOrganizations().getFirst().getId());
            bucketsApi.createBucket(bucket);
        } else {
            System.out.println("InfluxDB bucket already exists: " + bucketName);
        }
    }
}