#!/bin/bash
echo "wait DB container up"
dockerize -wait tcp://db:5432 -timeout 30s

# DB Migration
echo "run payment database migration"
flyway -configFiles=/flyway/conf/payment-migration.conf migrate

# Seed Migration
#echo "insert payment seed data"
#flyway -configFiles=./conf/payment-seed.conf migrate