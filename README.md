# Task Tracker API

REST API планировщика задач (микросервисы + Kafka).

## Сервисы

**backend** - API, JWT, задачи

**scheduler**  - отчёты по задачам (раз в полночь)

**email-sender** - отправка email

**postgres** - БД

**kafka**- брокер

## Технологии
Java 17 · Spring Boot 3.5.10 · JWT · Kafka · PostgreSQL · Liquibase · Docker

## Для тестирования

http://202.148.53.242:8080/swagger-ui/index.html#