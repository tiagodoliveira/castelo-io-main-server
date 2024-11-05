DO
'
DECLARE
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_catalog.pg_type WHERE typname = ''device_mode'') THEN
        CREATE TYPE device_mode AS ENUM (''AUTONOMOUS'', ''MANUAL'');
        CREATE CAST (varchar AS device_mode) WITH INOUT AS IMPLICIT;
    END IF;
END;' LANGUAGE 'plpgsql';

-- Create the User table
CREATE TABLE IF NOT EXISTS "User" (
    user_id SERIAL NOT NULL,
    user_name TEXT NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

-- Create the Gateway table
CREATE TABLE IF NOT EXISTS "Gateway" (
    gateway_mac VARCHAR(17) NOT NULL UNIQUE,
    gateway_user_id INTEGER,
    gateway_ip TEXT,
    gateway_name TEXT,
    CONSTRAINT pk_gateway PRIMARY KEY (gateway_mac),
    CONSTRAINT fk_gateway_user FOREIGN KEY ("gateway_user_id") REFERENCES "User"(user_id)
);

-- Create the Model table
CREATE TABLE IF NOT EXISTS "EndDeviceModel" (
    model_id INTEGER NOT NULL,
    latest_firmware_version TEXT NOT NULL,
    CONSTRAINT pk_model PRIMARY KEY (model_id)
);

-- Create the EndDevice table
CREATE TABLE IF NOT EXISTS "EndDevice" (
   end_device_mac VARCHAR(17) NOT NULL UNIQUE,
   end_device_ip TEXT NOT NULL,
   model_id INTEGER NOT NULL,
   end_device_name TEXT NOT NULL,
   debug_mode BOOLEAN NOT NULL DEFAULT FALSE,
   gateway_mac VARCHAR(17) NOT NULL,
   firmware TEXT NOT NULL,
   working_mode device_mode DEFAULT 'MANUAL',
   CONSTRAINT pk_enddevice PRIMARY KEY (end_device_mac),
   CONSTRAINT fk_enddevice_gateway FOREIGN KEY (gateway_mac) REFERENCES "Gateway"(gateway_mac),
   CONSTRAINT fk_enddevice_model FOREIGN KEY (model_id) REFERENCES "EndDeviceModel"(model_id)
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
    timestamp TIMESTAMP NOT NULL,
    switch_value BOOLEAN NOT NULL,
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
    timestamp TIMESTAMP NOT NULL,
    sensor_value TEXT NOT NULL,
    CONSTRAINT pk_sensor_state PRIMARY KEY (end_device_mac, sensor_number, time),
    CONSTRAINT fk_sensor FOREIGN KEY (end_device_mac, sensor_number) REFERENCES "Sensor"(end_device_mac, sensor_number)
);