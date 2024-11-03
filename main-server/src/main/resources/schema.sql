-- Create the User table
CREATE TABLE IF NOT EXISTS "User" (
    user_id SERIAL NOT NULL,
    user_name TEXT NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

-- Create the Gateway table
CREATE TABLE IF NOT EXISTS "Gateway" (
    gateway_mac VARCHAR(17) NOT NULL UNIQUE,
    user_id TEXT,
    gateway_ip TEXT,
    gateway_name TEXT,
    CONSTRAINT pk_gateway PRIMARY KEY (gateway_mac),
    CONSTRAINT fk_gateway_user FOREIGN KEY ("user_id") REFERENCES "User"(user_id)
);

-- Create the Model table
CREATE TABLE IF NOT EXISTS "Model" (
    model_id INTEGER NOT NULL,
    latest_firmware_version TEXT NOT NULL,
    CONSTRAINT pk_model PRIMARY KEY (model_id)
);

-- Create the EndDevice table
CREATE TABLE IF NOT EXISTS "EndDevice" (
    end_device_mac VARCHAR(17) NOT NULL UNIQUE,
    end_device_ip TEXT NOT NULL,
    model INTEGER NOT NULL,
    end_device_name TEXT NOT NULL,
    debug_mode BOOLEAN NOT NULL DEFAULT FALSE,
    gateway_mac VARCHAR(17) NOT NULL,
    firmware TEXT NOT NULL,
    CONSTRAINT pk_enddevice PRIMARY KEY (end_device_mac),
    CONSTRAINT fk_enddevice_gateway FOREIGN KEY (gateway_mac) REFERENCES "Gateway"(gateway_mac),
    CONSTRAINT fk_enddevice_model FOREIGN KEY (model) REFERENCES "Model"(model_id)
);

-- Create the Mode table
CREATE TABLE IF NOT EXISTS "Mode" (
    end_device_mac VARCHAR(17) NOT NULL,
    time TIMESTAMP NOT NULL,
    value VARCHAR(10) NOT NULL,
    CONSTRAINT pk_mode PRIMARY KEY (end_device_mac, time),
    CONSTRAINT fk_mode_enddevice FOREIGN KEY (end_device_mac) REFERENCES "EndDevice"(end_device_mac)
);

-- Create the Switch table
CREATE TABLE IF NOT EXISTS "Switch" (
    end_device_mac VARCHAR(17) NOT NULL,
    switch_number SMALLINT NOT NULL,
    switch_name TEXT NOT NULL,
    CONSTRAINT pk_switch PRIMARY KEY (end_device_mac, switch_number),
    CONSTRAINT fk_switch_enddevice FOREIGN KEY (end_device_mac) REFERENCES "EndDevice"(end_device_mac)
);

-- Create the SwitchState table
CREATE TABLE IF NOT EXISTS "SwitchState" (
    end_device_mac VARCHAR(17) NOT NULL,
    switch_number SMALLINT NOT NULL,
    time TIMESTAMP NOT NULL,
    value BOOLEAN NOT NULL,
    CONSTRAINT pk_switch_state PRIMARY KEY (end_device_mac, switch_number, time),
    CONSTRAINT fk_switch FOREIGN KEY (end_device_mac, switch_number) REFERENCES "Switch"(end_device_mac, switch_number)
);

-- Create the Sensor table
CREATE TABLE IF NOT EXISTS "Sensor" (
    end_device_mac VARCHAR(17) NOT NULL,
    sensor_number SMALLINT NOT NULL,
    sensor_name TEXT NOT NULL,
    CONSTRAINT pk_sensor PRIMARY KEY (end_device_mac, sensor_number),
    CONSTRAINT fk_sensor_enddevice FOREIGN KEY (end_device_mac) REFERENCES "EndDevice"(end_device_mac)
);

-- Create the SensorState table
CREATE TABLE IF NOT EXISTS "SensorState" (
    end_device_mac VARCHAR(17) NOT NULL,
    sensor_number SMALLINT NOT NULL,
    time TIMESTAMP NOT NULL,
    value TEXT NOT NULL,
    CONSTRAINT pk_sensor_state PRIMARY KEY (end_device_mac, sensor_number, time),
    CONSTRAINT fk_sensor FOREIGN KEY (end_device_mac, sensor_number) REFERENCES "Sensor"(mac, number)
);

-- Insert sample data into Gateway table
-- INSERT INTO "Gateway" (mac, ip, name) VALUES ('00-00-00-00-00-00', NULL, NULL), ('b8:27:eb:ec:5c:c2', NULL, NULL);