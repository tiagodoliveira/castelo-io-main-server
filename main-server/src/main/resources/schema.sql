-- Create the User table
CREATE TABLE "User" (
    id VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

-- Create the Gateway table
CREATE TABLE "Gateway" (
    id SERIAL NOT NULL,
    "user" VARCHAR(255),
    mac VARCHAR(17) NOT NULL UNIQUE,
    ip VARCHAR(45),
    name VARCHAR(25),
    CONSTRAINT pk_gateway PRIMARY KEY (id),
    CONSTRAINT fk_gateway_user FOREIGN KEY ("user") REFERENCES "User"(id)
);

-- Create the Profile table
CREATE TABLE "Profile" (
    id INTEGER NOT NULL,
    firmware VARCHAR(10) NOT NULL,
    CONSTRAINT pk_profile PRIMARY KEY (id)
);

-- Create the EndDevice table
CREATE TABLE "EndDevice" (
    mac VARCHAR(17) NOT NULL UNIQUE,
    ip VARCHAR(15) NOT NULL,
    profile INTEGER NOT NULL,
    name VARCHAR(20) NOT NULL,
    debug BOOLEAN NOT NULL DEFAULT FALSE,
    gateway INTEGER NOT NULL,
    firmware VARCHAR(10) NOT NULL,
    CONSTRAINT pk_enddevice PRIMARY KEY (mac),
    CONSTRAINT fk_enddevice_gateway FOREIGN KEY (gateway) REFERENCES "Gateway"(id),
    CONSTRAINT fk_enddevice_profile FOREIGN KEY (profile) REFERENCES "Profile"(id)
);

-- Create the Mode table
CREATE TABLE "Mode" (
    mac VARCHAR(17) NOT NULL,
    time TIMESTAMP(3) NOT NULL,
    value VARCHAR(10) NOT NULL,
    CONSTRAINT pk_mode PRIMARY KEY (mac, time),
    CONSTRAINT fk_mode_enddevice FOREIGN KEY (mac) REFERENCES "EndDevice"(mac)
);

-- Create the Switch table
CREATE TABLE "Switch" (
    mac VARCHAR(17) NOT NULL,
    number SMALLINT NOT NULL,
    name VARCHAR(30) NOT NULL,
    CONSTRAINT pk_switch PRIMARY KEY (mac, number),
    CONSTRAINT fk_switch_enddevice FOREIGN KEY (mac) REFERENCES "EndDevice"(mac)
);

-- Create the SwitchState table
CREATE TABLE "SwitchState" (
     mac VARCHAR(17) NOT NULL,
     number SMALLINT NOT NULL,
     time TIMESTAMP(3) NOT NULL,
     value VARCHAR(10) NOT NULL,
     CONSTRAINT pk_switch_state PRIMARY KEY (mac, number, time),
     CONSTRAINT fk_switch FOREIGN KEY (mac, number) REFERENCES "Switch"(mac, number)
);

-- Create the Sensor table
CREATE TABLE "Sensor" (
      mac VARCHAR(17) NOT NULL,
      number SMALLINT NOT NULL,
      name VARCHAR(30) NOT NULL,
      CONSTRAINT pk_sensor PRIMARY KEY (mac, number),
      CONSTRAINT fk_sensor_enddevice FOREIGN KEY (mac) REFERENCES "EndDevice"(mac)
);

-- Create the SensorState table
CREATE TABLE "SensorState" (
       mac VARCHAR(17) NOT NULL,
       number SMALLINT NOT NULL,
       time TIMESTAMP(3) NOT NULL,
       value VARCHAR(20) NOT NULL,
       CONSTRAINT pk_sensor_state PRIMARY KEY (mac, number, time),
       CONSTRAINT fk_sensor FOREIGN KEY (mac, number) REFERENCES "Sensor"(mac, number)
);

-- Insert sample data into Gateway table
-- INSERT INTO "Gateway" (mac, ip, name) VALUES ('00-00-00-00-00-00', NULL, NULL), ('b8:27:eb:ec:5c:c2', NULL, NULL);