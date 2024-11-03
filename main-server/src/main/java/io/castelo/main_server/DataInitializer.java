package io.castelo.main_server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.sql.Timestamp;

@Service
public class DataInitializer {

    @Autowired
    private JdbcClient jdbcClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void initializeData() throws Exception {
        JsonNode rootNode = objectMapper.readTree(new File("src/main/resources/data/data.json"));

        insertUsers(rootNode.get("User"));
        insertGateways(rootNode.get("Gateway"));
        insertProfiles(rootNode.get("Profile"));
        insertEndDevices(rootNode.get("EndDevice"));
        insertModes(rootNode.get("Mode"));
        insertSwitches(rootNode.get("Switch"));
        insertSwitchStates(rootNode.get("SwitchState"));
        insertSensors(rootNode.get("Sensor"));
        insertSensorStates(rootNode.get("SensorState"));
    }

    private void insertUsers(JsonNode users) {
        for (JsonNode user : users) {
            String id = user.get("id").asText();
            String name = user.get("name").asText();
            jdbcClient.sql("INSERT INTO User(id, name) VALUES (?, ?)").params(id, name).update();
        }
    }

    private void insertGateways(JsonNode gateways) {
        for (JsonNode gateway : gateways) {
            String userId = gateway.get("user").asText();
            String mac = gateway.get("mac").asText();
            String ip = gateway.get("ip").asText();
            String name = gateway.get("name").asText();
            jdbcClient.sql("INSERT INTO Gateway(user, mac, ip, name) VALUES (?, ?, ?, ?)")
                    .params(userId, mac, ip, name)
                    .update();
        }
    }

    private void insertProfiles(JsonNode profiles) {
        for (JsonNode profile : profiles) {
            int id = profile.get("id").asInt();
            String firmware = profile.get("firmware").asText();
            jdbcClient.sql("INSERT INTO Profile(id, firmware) VALUES (?, ?)").params(id, firmware).update();
        }
    }

    private void insertEndDevices(JsonNode endDevices) {
        for (JsonNode endDevice : endDevices) {
            String mac = endDevice.get("mac").asText();
            String ip = endDevice.get("ip").asText();
            int profile = endDevice.get("profile").asInt();
            String name = endDevice.get("name").asText();
            boolean debug = endDevice.get("debug").asBoolean();
            int gateway = endDevice.get("gateway").asInt();
            String firmware = endDevice.get("firmware").asText();
            jdbcClient.sql("INSERT INTO EndDevice(mac, ip, profile, name, debug, gateway, firmware) VALUES (?, ?, ?, ?, ?, ?, ?)")
                    .params(mac, ip, profile, name, debug, gateway, firmware)
                    .update();
        }
    }

    private void insertModes(JsonNode modes) {
        for (JsonNode mode : modes) {
            String mac = mode.get("mac").asText();
            String time = new Timestamp();

            String value = mode.get("value").asText();
            jdbcClient.sql("INSERT INTO Mode(mac, time, value) VALUES (?, ?, ?)").params(mac, time, value).update();
        }
    }

    private void insertSwitches(JsonNode switches) {
        for (JsonNode sw : switches) {
            String mac = sw.get("mac").asText();
            int number = sw.get("number").asInt();
            String name = sw.get("name").asText();
            jdbcClient.sql("INSERT INTO Switch(mac, number, name) VALUES (?, ?, ?)").params(mac, number, name).update();
        }
    }

    private void insertSwitchStates(JsonNode switchStates) {
        for (JsonNode switchState : switchStates) {
            String mac = switchState.get("mac").asText();
            int number = switchState.get("number").asInt();
            String time = switchState.get("time").asText();
            String value = switchState.get("value").asText();
            jdbcClient.sql("INSERT INTO SwitchState(mac, number, time, value) VALUES (?, ?, ?, ?)")
                    .params(mac, number, time, value).update();
        }
    }

    private void insertSensors(JsonNode sensors) {
        for (JsonNode sensor : sensors) {
            String mac = sensor.get("mac").asText();
            int number = sensor.get("number").asInt();
            String name = sensor.get("name").asText();
            jdbcClient.sql("INSERT INTO Sensor(mac, number, name) VALUES (?, ?, ?)").params(mac, number, name).update();
        }
    }

    private void insertSensorStates(JsonNode sensorStates) {
        for (JsonNode sensorState : sensorStates) {
            String mac = sensorState.get("mac").asText();
            int number = sensorState.get("number").asInt();
            String time = sensorState.get("time").asText();
            String value = sensorState.get("value").asText();
            jdbcClient.sql("INSERT INTO SensorState(mac, number, time, value) VALUES (?, ?, ?, ?)")
                    .params(mac, number, time, value).update();
        }
    }
}
