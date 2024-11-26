package io.castelo.main_server.database;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class PostgresDataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(PostgresDataInitializer.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    @Transactional
    public void initializeData() throws IOException {
        logger.info("Initializing database data");
        try {
            JsonNode rootNode = objectMapper.readTree(new File("src/main/resources/data/database-init-data.json"));

            insertUsers(rootNode.get("User"));
            insertGateways(rootNode.get("Gateway"));
            insertEndDevicelModels(rootNode.get("EndDeviceModel"));
            insertSensorModel(rootNode.get("SensorModel"));
            insertSwitchModel(rootNode.get("SwitchModel"));
            insertEndDevices(rootNode.get("EndDevice"));
            insertSwitches(rootNode.get("Switch"));
            insertSensors(rootNode.get("Sensor"));
        } catch (Exception e) {
            logger.error("Data initialization failed", e);
            throw e;
        }
    }

    private void insertSwitchModel(JsonNode switchModel) {
        switchModel.forEach(switchNode -> {
            int modelId = switchNode.get("model_id").asInt();
            int switchNumber = switchNode.get("switch_number").asInt();
            String switchName = switchNode.get("switch_name").asText();

            jdbcTemplate.update(
                    "INSERT INTO switch_models (model_id, switch_number, switch_name) " +
                            "VALUES (?, ?, ?) " +
                            "ON CONFLICT (model_id, switch_number) DO NOTHING",
                    modelId, switchNumber, switchName);
        });
    }

    private void insertSensorModel(JsonNode sensorModel) {
        sensorModel.forEach(sensorNode -> {
            int modelId = sensorNode.get("model_id").asInt();
            int sensorNumber = sensorNode.get("sensor_number").asInt();
            String sensorName = sensorNode.get("sensor_name").asText();

            jdbcTemplate.update(
                    "INSERT INTO sensor_models (model_id, sensor_number, sensor_name) " +
                            "VALUES (?, ?, ?) " +
                            "ON CONFLICT (model_id, sensor_number) DO NOTHING",
                    modelId, sensorNumber, sensorName);
        });
    }

    private void insertUsers(JsonNode users) {
        for (JsonNode user : users) {
            UUID userId = UUID.fromString(user.get("user_id").asText());
            String userName = user.get("user_name").asText();
            jdbcTemplate.update(
                    "INSERT INTO users (user_id, user_name) " +
                    "VALUES (?, ?) " +
                    "ON CONFLICT DO NOTHING", userId, userName);
        }
    }

    private void insertGateways(JsonNode gateways) {
        for (JsonNode gateway : gateways) {
            String gatewayMac = gateway.get("gateway_mac").asText();
            UUID userId = UUID.fromString(gateway.get("user_id").asText());
            String gatewayIp = gateway.get("gateway_ip").asText();
            String gatewayName = gateway.get("gateway_name").asText();

            jdbcTemplate.update(
                    "INSERT INTO gateways (gateway_mac, gateway_user_id, gateway_ip, gateway_name) " +
                            "VALUES (?, ?, CAST(? AS inet), ?) " +
                            "ON CONFLICT (gateway_mac) DO NOTHING",
                    gatewayMac, userId, gatewayIp, gatewayName
            );
        }
    }

    private void insertEndDevicelModels(JsonNode models) {
        for (JsonNode model : models) {
            int modelId = model.get("model_id").asInt();
            String latestFirmwareVersion = model.get("latest_firmware_version").asText();

            jdbcTemplate.update(
                    "INSERT INTO end_device_models (model_id, latest_firmware_version) " +
                            "VALUES (?, ?) " +
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
            UUID userId = UUID.fromString(endDevice.get("user_id").asText());
            boolean debugMode = endDevice.get("debug_mode").asBoolean();
            String gatewayMac = endDevice.get("gateway_mac").asText();
            String firmware = endDevice.get("firmware").asText();
            String workingMode = endDevice.get("working_mode").asText();

            jdbcTemplate.update(
                    "INSERT INTO end_devices (end_device_mac, end_device_ip, model_id, end_device_name, debug_mode, gateway_mac, firmware, working_mode, user_id) " +
                            "VALUES (?, CAST(? AS inet), ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (end_device_mac) DO NOTHING",
                    endDeviceMac, endDeviceIp, modelId, endDeviceName, debugMode, gatewayMac, firmware, workingMode, userId
            );
        }
    }

    private void insertSwitches(JsonNode switches) {
        for (JsonNode switchNode : switches) {
            String endDeviceMac = switchNode.get("end_device_mac").asText();
            int switchNumber = switchNode.get("switch_number").asInt();
            String switchName = switchNode.get("switch_name").asText();

            jdbcTemplate.update(
                    "INSERT INTO switches (end_device_mac, switch_number, switch_name) VALUES (?, ?, ?) " +
                            "ON CONFLICT (end_device_mac, switch_number) DO NOTHING",
                    endDeviceMac, switchNumber, switchName
            );
        }
    }

    private void insertSensors(JsonNode sensors) {
        for (JsonNode sensor : sensors) {
            String endDeviceMac = sensor.get("end_device_mac").asText();
            int sensorNumber = sensor.get("sensor_number").asInt();
            String sensorName = sensor.get("sensor_name").asText();

            jdbcTemplate.update(
                    "INSERT INTO sensors (end_device_mac, sensor_number, sensor_name) VALUES (?, ?, ?) " +
                            "ON CONFLICT (end_device_mac, sensor_number) DO NOTHING",
                    endDeviceMac, sensorNumber, sensorName
            );
        }
    }
}