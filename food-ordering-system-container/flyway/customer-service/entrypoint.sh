#!/bin/bash

echo "wait DB container up"
dockerize -wait tcp://db:5432 -timeout 30s

## DB Migration
echo "run customer database migration"
flyway -configFiles=/flyway/conf/customer-migration.conf migrate

# Seed Migration
echo "insert customer seed data"
flyway -configFiles=/flyway/conf/customer-seed.conf migrate