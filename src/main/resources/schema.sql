DO
'
DECLARE
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_catalog.pg_type WHERE typname = ''working_modes'') THEN
        CREATE TYPE working_modes AS ENUM (''AUTONOMOUS'', ''MANUAL'');
        CREATE CAST (varchar AS working_modes) WITH INOUT AS IMPLICIT;
        CREATE CAST (CHARACTER VARYING as inet) WITH INOUT AS IMPLICIT;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_catalog.pg_type WHERE typname = ''component_types'') THEN
        CREATE TYPE component_types AS ENUM (''ANALOG_SENSOR'', ''DIGITAL_SENSOR'', ''ANALOG_SWITCH'', ''DIGITAL_SWITCH'');
        CREATE CAST (varchar AS component_types) WITH INOUT AS IMPLICIT;
    END IF;
END;' LANGUAGE 'plpgsql';

-- Create the User table
CREATE TABLE IF NOT EXISTS "users" (
    user_id uuid NOT NULL PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    display_name TEXT,
    role TEXT NOT NULL DEFAULT 'USER',
    is_credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    is_user_enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create the Gateway table
CREATE TABLE IF NOT EXISTS "gateways" (
    gateway_mac VARCHAR(17) PRIMARY KEY,
    owner_id UUID NOT NULL REFERENCES "users"(user_id),
    gateway_ip inet,
    gateway_name TEXT
);

-- Create the Model table
CREATE TABLE IF NOT EXISTS "end_device_models" (
    model_id INTEGER PRIMARY KEY,
    latest_firmware_version TEXT NOT NULL
);

-- Create the Sensor Model table
CREATE TABLE IF NOT EXISTS "end_device_component_models" (
    model_id INTEGER NOT NULL REFERENCES "end_device_models"(model_id) ON DELETE CASCADE,
    component_type component_types NOT NULL,
    component_number SMALLINT NOT NULL,
    component_name TEXT NOT NULL,
    PRIMARY KEY (model_id, component_number)
);

-- Create the EndDevice table
CREATE TABLE IF NOT EXISTS "end_devices" (
    end_device_mac VARCHAR(17) PRIMARY KEY,
    end_device_ip inet,
    model_id INTEGER NOT NULL REFERENCES "end_device_models"(model_id),
    end_device_name TEXT NOT NULL,
    debug_mode BOOLEAN NOT NULL DEFAULT FALSE,
    gateway_mac VARCHAR(17) REFERENCES "gateways"(gateway_mac) ON DELETE CASCADE,
    firmware TEXT NOT NULL,
    working_mode working_modes DEFAULT 'MANUAL',
    owner_id uuid NOT NULL REFERENCES "users"(user_id)
);

-- Create the Sensor table
CREATE TABLE IF NOT EXISTS "end_device_components" (
    end_device_mac VARCHAR(17) NOT NULL REFERENCES "end_devices"(end_device_mac) ON DELETE CASCADE,
    component_type component_types NOT NULL,
    component_number SMALLINT NOT NULL,
    component_name TEXT NOT NULL,
    PRIMARY KEY (end_device_mac, component_number)
);

-- Create the End Device shared users table
CREATE TABLE IF NOT EXISTS "end_device_shared_users" (
    end_device_mac VARCHAR(17) NOT NULL,
    user_id UUID NOT NULL,
    PRIMARY KEY (end_device_mac, user_id),
    FOREIGN KEY (end_device_mac) REFERENCES end_devices(end_device_mac) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create the Gateway shared users table
CREATE TABLE IF NOT EXISTS "gateway_shared_users" (
    gateway_mac VARCHAR(17) NOT NULL,
    user_id UUID NOT NULL,
    PRIMARY KEY (gateway_mac, user_id),
    FOREIGN KEY (gateway_mac) REFERENCES gateways(gateway_mac) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_end_device_users_user ON end_device_shared_users(user_id);
CREATE INDEX IF NOT EXISTS idx_end_device_users_device ON end_device_shared_users(end_device_mac);

CREATE INDEX IF NOT EXISTS idx_gateway_users_user ON gateway_shared_users(user_id);
CREATE INDEX IF NOT EXISTS idx_gateway_users_device ON gateway_shared_users(gateway_mac);