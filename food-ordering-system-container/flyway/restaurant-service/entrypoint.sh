#!/bin/bash
echo "wait DB container up"
dockerize -wait tcp://db:5432 -timeout 30s

# DB Migration
echo "run restaurant database migration"
flyway -configFiles=/flyway/conf/restaurant-migration.conf migrate

# Seed Migration
echo "insert restaurant seed data"
flyway -configFiles=/flyway/conf/restaurant-seed.conf migrate