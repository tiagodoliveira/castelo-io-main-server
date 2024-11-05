package io.castelo.main_server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.sql.Types;

@Service
public class DataInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void initializeData() throws IOException {
        JsonNode rootNode = objectMapper.readTree(new File("src/main/resources/data/database-init-data.json"));

        insertUsers(rootNode.get("User"));
        insertGateways(rootNode.get("Gateway"));
        insertModels(rootNode.get("Model"));
        insertEndDevices(rootNode.get("EndDevice"));
        insertSwitches(rootNode.get("Switch"));
        insertSwitchStates(rootNode.get("SwitchState"));
        insertSensors(rootNode.get("Sensor"));
        insertSensorStates(rootNode.get("SensorState"));
    }

    private void insertUsers(JsonNode users) {
        for (JsonNode user : users) {
            String userName = user.get("user_name").asText();
            jdbcTemplate.update("INSERT INTO \"User\" (user_name) VALUES (?) ", userName);
        }
    }

    private void insertGateways(JsonNode gateways) {
        for (JsonNode gateway : gateways) {
            String gatewayMac = gateway.get("gateway_mac").asText();
            Integer gatewayUserId = gateway.has("gateway_user_id") ? gateway.get("gateway_user_id").asInt() : null;
            String gatewayIp = gateway.has("gateway_ip") ? gateway.get("gateway_ip").asText() : null;
            String gatewayName = gateway.get("gateway_name").asText();

            jdbcTemplate.update(
                    "INSERT INTO \"Gateway\" (gateway_mac, gateway_user_id, gateway_ip, gateway_name) VALUES (?, ?, ?, ?) " +
                            "ON CONFLICT (gateway_mac) DO NOTHING",
                    gatewayMac, gatewayUserId, gatewayIp, gatewayName
            );
        }
    }

    private void insertModels(JsonNode models) {
        for (JsonNode model : models) {
            int modelId = model.get("model_id").asInt();
            String latestFirmwareVersion = model.get("latest_firmware_version").asText();

            jdbcTemplate.update(
                    "INSERT INTO \"Model\" (model_id, latest_firmware_version) VALUES (?, ?) " +
                            "ON CONFLICT (model_id) DO NOTHING",
                    modelId, latestFirmwareVersion
            );
        }
    }

    private void insertEndDevices(JsonNode endDevices) {
        for (JsonNode endDevice : endDevices) {
            String endDeviceMac = endDevice.get("end_device_mac").asText();
            String endDeviceIp = endDevice.get("end_device_ip").asText();
            int modelId = endDevice.get("model_id").asInt();
            String endDeviceName = endDevice.get("end_device_name").asText();
            boolean debugMode = endDevice.get("debug_mode").asBoolean();
            String gatewayMac = endDevice.get("gateway_mac").asText();
            String firmware = endDevice.get("firmware").asText();
            String workingMode = endDevice.get("working_mode").asText();

            jdbcTemplate.update(
                    "INSERT INTO \"EndDevice\" (end_device_mac, end_device_ip, model_id, end_device_name, debug_mode, gateway_mac, firmware, working_mode) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (end_device_mac) DO NOTHING",
                    endDeviceMac, endDeviceIp, modelId, endDeviceName, debugMode, gatewayMac, firmware, workingMode
            );
        }
    }

    private void insertSwitches(JsonNode switches) {
        for (JsonNode switchNode : switches) {
            String endDeviceMac = switchNode.get("end_device_mac").asText();
            int switchNumber = switchNode.get("switch_number").asInt();
            String switchName = switchNode.get("switch_name").asText();

            jdbcTemplate.update(
                    "INSERT INTO \"Switch\" (end_device_mac, switch_number, switch_name) VALUES (?, ?, ?) " +
                            "ON CONFLICT (end_device_mac, switch_number) DO NOTHING",
                    endDeviceMac, switchNumber, switchName
            );
        }
    }

    private void insertSwitchStates(JsonNode switchStates) {
        for (JsonNode switchState : switchStates) {
            String endDeviceMac = switchState.get("end_device_mac").asText();
            int switchNumber = switchState.get("switch_number").asInt();
            String time = switchState.get("timestamp").asText();
            boolean value = switchState.get("value").asBoolean();

            String sql = "INSERT INTO \"SwitchState\" (end_device_mac, switch_number, time, value) VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (end_device_mac, switch_number, time) DO NOTHING";

            Object[] params = {endDeviceMac, switchNumber, time, value};
            int[] argumentTypes = {java.sql.Types.VARCHAR, java.sql.Types.INTEGER, Types.TIMESTAMP, java.sql.Types.BOOLEAN};

            jdbcTemplate.update(sql, params, argumentTypes);
        }
    }

    private void insertSensors(JsonNode sensors) {
        for (JsonNode sensor : sensors) {
            String endDeviceMac = sensor.get("end_device_mac").asText();
            int sensorNumber = sensor.get("sensor_number").asInt();
            String sensorName = sensor.get("sensor_name").asText();

            jdbcTemplate.update(
                    "INSERT INTO \"Sensor\" (end_device_mac, sensor_number, sensor_name) VALUES (?, ?, ?) " +
                            "ON CONFLICT (end_device_mac, sensor_number) DO NOTHING",
                    endDeviceMac, sensorNumber, sensorName
            );
        }
    }

    private void insertSensorStates(JsonNode sensorStates) {
        for (JsonNode sensorState : sensorStates) {
            String endDeviceMac = sensorState.get("end_device_mac").asText();
            int sensorNumber = sensorState.get("sensor_number").asInt();
            String time = sensorState.get("timestamp").asText();
            String value = sensorState.get("value").asText();

            jdbcTemplate.update(
                    "INSERT INTO \"SensorState\" (end_device_mac, sensor_number, time, value) VALUES (?, ?, ?, ?) " +
                            "ON CONFLICT (end_device_mac, sensor_number, time) DO NOTHING",
                    endDeviceMac, sensorNumber, time, value
            );
        }
    }
}