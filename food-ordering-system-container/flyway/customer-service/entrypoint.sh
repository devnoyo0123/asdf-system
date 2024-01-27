#!/bin/bash

echo "-----------------------"
DIR=$( pwd )
echo $DIR

LS=$( ls -al )
echo $LS



echo "-----------------------"
cd /flyway/conf

DIR=$( pwd )
echo $DIR

LS=$( ls -al )
echo $LS


echo "wait DB container up"
dockerize -wait tcp://db:5432 -timeout 20s

## DB Migration
echo "run customer database migration"
flyway -configFiles=/flyway/conf/customer-migration.conf migrate
#
## Seed Migration
#echo "insert customer seed data"
#flyway -configFiles=/flyway/customer-service/conf/customer-seed.conf migrate