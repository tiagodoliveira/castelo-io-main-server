-- Drop foreign keys in PostgreSQL
ALTER TABLE "homesense"."Gateway" DROP CONSTRAINT fk_gateway_user;
ALTER TABLE "homesense"."EndDevice" DROP CONSTRAINT fk_enddevice_gateway;
ALTER TABLE "homesense"."EndDevice" DROP CONSTRAINT fk_enddevice_profile;
ALTER TABLE "homesense"."Mode" DROP CONSTRAINT fk_mode_enddevice;
ALTER TABLE "homesense"."Switch" DROP CONSTRAINT fk_switch_enddevice;
ALTER TABLE "homesense"."SwitchState" DROP CONSTRAINT fk_switch;

-- Drop tables in PostgreSQL
DROP TABLE IF EXISTS "homesense"."User";
DROP TABLE IF EXISTS "homesense"."Gateway";
DROP TABLE IF EXISTS "homesense"."Profile";
DROP TABLE IF EXISTS "homesense"."EndDevice";
DROP TABLE IF EXISTS "homesense"."Mode";
DROP TABLE IF EXISTS "homesense"."Switch";
DROP TABLE IF EXISTS "homesense"."SwitchState";
DROP TABLE IF EXISTS "homesense"."Sensor";
DROP TABLE IF EXISTS "homesense"."SensorState";

-- Drop schema in PostgreSQL
DROP SCHEMA IF EXISTS "homesense" CASCADE;