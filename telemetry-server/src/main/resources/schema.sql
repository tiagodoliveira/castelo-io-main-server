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
    user_id uuid NOT NULL PRIMARY KEY,
    user_name TEXT NOT NULL
);

-- Create the Gateway table
CREATE TABLE IF NOT EXISTS "Gateway" (
    gateway_mac VARCHAR(17) PRIMARY KEY,
    gateway_user_id UUID NOT NULL REFERENCES "User"(user_id),
    gateway_ip inet,
    gateway_name TEXT
);

-- Create the Model table
CREATE TABLE IF NOT EXISTS "EndDeviceModel" (
    model_id INTEGER PRIMARY KEY,
    latest_firmware_version TEXT NOT NULL
);

-- Create the EndDevice table
CREATE TABLE IF NOT EXISTS "EndDevice" (
   end_device_mac VARCHAR(17) PRIMARY KEY,
   end_device_ip TEXT NOT NULL,
   model_id INTEGER NOT NULL REFERENCES "EndDeviceModel"(model_id),
   end_device_name TEXT NOT NULL,
   debug_mode BOOLEAN NOT NULL DEFAULT FALSE,
   gateway_mac VARCHAR(17) NOT NULL REFERENCES "Gateway"(gateway_mac),
   firmware TEXT NOT NULL,
   working_mode device_mode DEFAULT 'MANUAL'
);

-- Create the Switch table
CREATE TABLE IF NOT EXISTS "Switch" (
    end_device_mac VARCHAR(17) NOT NULL REFERENCES "EndDevice"(end_device_mac),
    switch_number SMALLINT NOT NULL,
    switch_name TEXT NOT NULL,
    PRIMARY KEY (end_device_mac, switch_number)
);

-- Create the Sensor table
CREATE TABLE IF NOT EXISTS "Sensor" (
    end_device_mac VARCHAR(17) NOT NULL REFERENCES "EndDevice"(end_device_mac),
    sensor_number SMALLINT NOT NULL,
    sensor_name TEXT NOT NULL,
    PRIMARY KEY (end_device_mac, sensor_number)
);