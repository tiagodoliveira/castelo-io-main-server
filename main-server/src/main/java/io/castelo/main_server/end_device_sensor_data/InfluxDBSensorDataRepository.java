package io.castelo.main_server.end_device_sensor_data;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.query.FluxRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class InfluxDBSensorDataRepository implements SensorDataRepository {

    private final InfluxDBClient influxDBClient;

    @Autowired
    public InfluxDBSensorDataRepository(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    @Override
    public void addSensorData(EndDeviceSensorData sensorData) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        List<Point> points = sensorData.sensors().stream()
                .flatMap(sensor -> sensor.sensorValues().stream()
                        .map(sensorValue -> Point.measurement("sensor_data")
                                .addTag("end_device_mac", sensorData.endDeviceMac())
                                .addTag("sensor_number", Integer.toString(sensor.sensorNumber()))
                                .addField("sensor_value", sensorValue.value())
                                .time(sensorValue.timestamp().atZone(ZoneId.of("UTC")).toInstant(), WritePrecision.MS))
                )
                .collect(Collectors.toList());

        writeApi.writePoints(points);
    }

    @Override
    public EndDeviceSensorData getSensorDataBySensorNumber(LocalDateTime start, LocalDateTime stop, String macAddress, int sensorNumber) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String formattedStart = start.format(formatter);
        String formattedStop = stop.format(formatter);

        QueryApi queryApi = influxDBClient.getQueryApi();
        String fluxQuery = String.format(
                "from(bucket:\"sensor_data\") " +
                        "|> range(start: \"%s\", stop: \"%s\") " +
                        "|> filter(fn: (r) => r[\"end_device_mac\"] == \"%s\" and r[\"sensor_number\"] == \"%d\")",
                formattedStart, formattedStop, macAddress, sensorNumber);

        List<SensorValue> sensorValues = queryApi.query(fluxQuery).stream()
                .flatMap(table -> table.getRecords().stream())
                .map(InfluxDBSensorDataRepository::apply)
                .filter(Objects::nonNull) // Filter out null values
                .collect(Collectors.toList());

        if (sensorValues.isEmpty()) {
            return null; // or handle as appropriate
        }

        Sensor sensor = new Sensor(sensorNumber, sensorValues);
        return new EndDeviceSensorData(macAddress, List.of(sensor));
    }

    @Override
    public EndDeviceSensorData getAllSensorValues(String macAddress) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        String fluxQuery = String.format(
                "from(bucket:\"sensor_data\") " +
                        "|> filter(fn: (r) => r[\"end_device_mac\"] == \"%s\")",
                macAddress);

        List<Sensor> sensors = queryApi.query(fluxQuery).stream()
                .flatMap(table -> table.getRecords().stream())
                .collect(Collectors.groupingBy(record -> record.getValueByKey("sensor_number")))
                .entrySet().stream()
                .map(entry -> {
                    int sensorNumber = Integer.parseInt((String) entry.getKey());
                    List<SensorValue> sensorValues = entry.getValue().stream()
                            .map(InfluxDBSensorDataRepository::apply)
                            .filter(Objects::nonNull) // Filter out null values
                            .collect(Collectors.toList());
                    return new Sensor(sensorNumber, sensorValues);
                })
                .collect(Collectors.toList());

        if (sensors.isEmpty()) {
            return null; // or handle as appropriate
        }

        return new EndDeviceSensorData(macAddress, sensors);
    }

    private static SensorValue apply(FluxRecord record) {
        Instant time = record.getTime();
        Object valueObj = record.getValueByKey("sensor_value");

        if (time != null && valueObj instanceof String value) {
            LocalDateTime timestamp = LocalDateTime.ofInstant(time, ZoneId.of("UTC"));
            return new SensorValue(timestamp, value);
        }

        return null; // Return null for invalid records
    }
}