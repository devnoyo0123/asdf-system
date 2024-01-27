#!/bin/bash
echo "wait DB container up"
dockerize -wait tcp://db:5432 -timeout 20s

pwd

# DB Migration
#echo "run restaurant database migration"
#flyway -configFiles=/flyway/customer-service/conf/restaurant-migration.conf migrate

# Seed Migration
#echo "insert restaurant seed data"
#flyway -configFiles=./conf/restaurant-seed.conf migrate