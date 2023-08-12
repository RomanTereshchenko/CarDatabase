CREATE SCHEMA IF NOT EXISTS car_db;

CREATE TABLE IF NOT EXISTS car_db.cars
(
	id SERIAL PRIMARY KEY,
	object_id VARCHAR(50),
	make VARCHAR(50),
	year SMALLINT,
	model VARCHAR(50),
	category VARCHAR(50)
);