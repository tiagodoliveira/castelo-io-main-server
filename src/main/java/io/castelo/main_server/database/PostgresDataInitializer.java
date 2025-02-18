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
            insertEndDevicelModels(rootNode.get("EndDeviceModel"));
            insertEndDeviceComponentModel(rootNode.get("EndDeviceComponentModel"));
            insertUsers(rootNode.get("User"));
            insertGateways(rootNode.get("Gateway"));
            insertEndDevices(rootNode.get("EndDevice"));
            insertEndDeviceComponent(rootNode.get("EndDeviceComponent"));
        } catch (Exception e) {
            logger.error("Data initialization failed", e);
            throw e;
        }
    }

    private void insertEndDeviceComponentModel(JsonNode componentModel) {
        componentModel.forEach(componentNode -> {
            int modelId = componentNode.get("model_id").asInt();
            String componentType = componentNode.get("component_type").asText();
            int componentNumber = componentNode.get("component_number").asInt();
            String componentName = componentNode.get("component_name").asText();

            jdbcTemplate.update(
                    "INSERT INTO end_device_component_models (model_id, component_type, component_number, component_name) " +
                            "VALUES (?, ?, ?, ?) " +
                            "ON CONFLICT (model_id, component_number) DO NOTHING",
                    modelId, componentType, componentNumber, componentName);
        });
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

    private void insertUsers(JsonNode users) {
        for (JsonNode user : users) {
            UUID userId = UUID.fromString(user.get("user_id").asText());
            String email = user.get("email").asText();
            String password = user.get("password").asText();
            String displayName = user.get("display_name").asText();
            String role = user.get("role").asText();

            jdbcTemplate.update(
                    "INSERT INTO users (user_id, email, password, display_name, role) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT DO NOTHING", userId, email, password, displayName, role);
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

    private void insertEndDevices(JsonNode endDevices) {
        for (JsonNode endDevice : endDevices) {
            String endDeviceMac = endDevice.get("end_device_mac").asText();
            String endDeviceIp = endDevice.get("end_device_ip").asText();
            int modelId = endDevice.get("model_id").asInt();
            String endDeviceName = endDevice.get("end_device_name").asText();
            UUID owner_id = UUID.fromString(endDevice.get("owner_id").asText());
            boolean debugMode = endDevice.get("debug_mode").asBoolean();
            String gatewayMac = endDevice.get("gateway_mac").asText();
            String firmware = endDevice.get("firmware").asText();
            String workingMode = endDevice.get("working_mode").asText();

            jdbcTemplate.update(
                    "INSERT INTO end_devices (end_device_mac, end_device_ip, model_id, end_device_name, debug_mode, gateway_mac, firmware, working_mode, owner_id) " +
                            "VALUES (?, CAST(? AS inet), ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (end_device_mac) DO NOTHING",
                    endDeviceMac, endDeviceIp, modelId, endDeviceName, debugMode, gatewayMac, firmware, workingMode, owner_id
            );
        }
    }

    private void insertEndDeviceComponent(JsonNode endDeviceComponents) {
        for (JsonNode endDeviceComponent : endDeviceComponents) {
            String endDeviceMac = endDeviceComponent.get("end_device_mac").asText();
            String componentType = endDeviceComponent.get("component_type").asText();
            int componentNumber = endDeviceComponent.get("component_number").asInt();
            String componentName = endDeviceComponent.get("component_name").asText();

            jdbcTemplate.update(
                    "INSERT INTO end_device_components (end_device_mac, component_type, component_number, component_name) VALUES (?, ?, ?, ?) " +
                            "ON CONFLICT (end_device_mac, component_number) DO NOTHING",
                    endDeviceMac, componentType, componentNumber, componentName
            );
        }
    }
}