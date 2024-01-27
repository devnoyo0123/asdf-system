#!/bin/bash
echo "wait DB container up"
dockerize -wait tcp://db:5432 -timeout 20s

# DB Migration
echo "run payment database migration"
flyway -configFiles=/flyway/customer-service/conf/payment-migration.conf migrate

# Seed Migration
#echo "insert payment seed data"
#flyway -configFiles=./conf/payment-seed.conf migrate