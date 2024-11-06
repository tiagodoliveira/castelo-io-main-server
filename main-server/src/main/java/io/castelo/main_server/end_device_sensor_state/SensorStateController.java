package io.castelo.main_server.end_device_sensor_state;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class SensorStateController {

    @Autowired
    private InfluxDBClient influxDBClient;

    @PostMapping("/add-sensor-data")
    public String addSensorData(@RequestParam String endDeviceMac,
                                @RequestParam int sensorNumber,
                                @RequestParam double value) {

        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

        Point point = Point.measurement("sensor_state")
                .addTag("end_device_mac", endDeviceMac)
                .addTag("sensor_number", Integer.toString(sensorNumber))
                .addField("value", value)
                .time(Instant.now(), WritePrecision.MS);

        writeApi.writePoint(point);
        return "Data inserted";
    }

    @GetMapping("/get-sensor-data")
    public List<Map<String, Object>> getSensorData() {
        QueryApi queryApi = influxDBClient.getQueryApi();

        String fluxQuery = "from(bucket:\"sensor_data\") |> range(start: -1h)";

        List<Map<String, Object>> records = queryApi.query(fluxQuery).stream()
                .flatMap(table -> table.getRecords().stream())
                .map(record -> record.getValues())
                .collect(Collectors.toList());

        return records;
    }
}