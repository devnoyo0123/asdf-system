#!/bin/bash
echo "wait DB container up"
dockerize -wait tcp://db:5432 -timeout 20s

# DB Migration
echo "run order database migration"
flyway -configFiles=/flyway/order-service/conf/order-migration.conf migrate

# Seed Migration
#echo "insert order seed data"
#flyway -configFiles=./conf/order-seed.conf migrate