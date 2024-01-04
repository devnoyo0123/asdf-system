# Makefile

# 서비스 시작
up:
	docker-compose up -d

# 서비스 정지
down:
	docker-compose down

# 전체 서비스 로그 보기
logs:
	docker-compose logs

# Zookeeper 로그 보기
logs-zookeeper:
	docker-compose logs zookeeper

# Schema Registry 로그 보기
logs-schema-registry:
	docker-compose logs schema-registry

# Kafka Broker 1 로그 보기
logs-kafka-broker-1:
	docker-compose logs kafka-broker-1

# Kafka Broker 2 로그 보기
logs-kafka-broker-2:
	docker-compose logs kafka-broker-2

# Kafka Broker 3 로그 보기
logs-kafka-broker-3:
	docker-compose logs kafka-broker-3

# Kafka UI 로그 보기
logs-kafka-ui:
	docker-compose logs kafka-ui

# Init Kafka 로그 보기
logs-init-kafka:
	docker-compose logs init-kafka

# PostgreSQL DB 로그 보기
logs-db:
	docker-compose logs db

# PostgreSQL 데이터 초기화
reset-db:
	docker-compose stop db && \
	docker-compose rm db && \
	rm -rf ./db/pgdata && \
	docker-compose up -d db

up-kafka-ui:
	docker-compose up -d kafka-ui

down-kafka-ui:
	docker-compose stop kafka-ui && \
	docker-compose rm kafka-ui

# 도움말
help:
	@echo "사용 가능한 명령어:"
	@echo "  make up                   : 서비스 시작"
	@echo "  make down                 : 서비스 정지"
	@echo "  make logs                 : 전체 서비스 로그 확인"
	@echo "  make logs-zookeeper       : Zookeeper 로그 확인"
	@echo "  make logs-schema-registry : Schema Registry 로그 확인"
	@echo "  make logs-kafka-broker-1  : Kafka Broker 1 로그 확인"
	@echo "  make logs-kafka-broker-2  : Kafka Broker 2 로그 확인"
	@echo "  make logs-kafka-broker-3  : Kafka Broker 3 로그 확인"
	@echo "  make logs-kafka-ui        : Kafka UI 로그 확인"
	@echo "  make logs-init-kafka      : Init Kafka 로그 확인"
	@echo "  make logs-db              : PostgreSQL DB 로그 확인"
	@echo "  make reset-db             : PostgreSQL 데이터 초기화"
	@echo "  make up-kafka-ui          : kafka UI 서비스 시작"
	@echo "  make down-kafka-ui        : kafka UI 서비스 정지"
