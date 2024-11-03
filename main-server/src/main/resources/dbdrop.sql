-- Drop foreign keys in PostgreSQL
ALTER TABLE "casatelo-io-database"."Gateway" DROP CONSTRAINT fk_gateway_user;
ALTER TABLE "casatelo-io-database"."EndDevice" DROP CONSTRAINT fk_enddevice_gateway;
ALTER TABLE "casatelo-io-database"."EndDevice" DROP CONSTRAINT fk_enddevice_profile;
ALTER TABLE "casatelo-io-database"."Mode" DROP CONSTRAINT fk_mode_enddevice;
ALTER TABLE "casatelo-io-database"."Switch" DROP CONSTRAINT fk_switch_enddevice;
ALTER TABLE "casatelo-io-database"."SwitchState" DROP CONSTRAINT fk_switch;

-- Drop tables in PostgreSQL
DROP TABLE IF EXISTS "casatelo-io-database"."User";
DROP TABLE IF EXISTS "casatelo-io-database"."Gateway";
DROP TABLE IF EXISTS "casatelo-io-database"."Profile";
DROP TABLE IF EXISTS "casatelo-io-database"."EndDevice";
DROP TABLE IF EXISTS "casatelo-io-database"."Mode";
DROP TABLE IF EXISTS "casatelo-io-database"."Switch";
DROP TABLE IF EXISTS "casatelo-io-database"."SwitchState";
DROP TABLE IF EXISTS "casatelo-io-database"."Sensor";
DROP TABLE IF EXISTS "casatelo-io-database"."SensorState";

-- Drop schema in PostgreSQL
DROP SCHEMA IF EXISTS "casatelo-io-database" CASCADE;