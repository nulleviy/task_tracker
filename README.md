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
**Для теста работы приложения есть документация но рекомендую тестировать через постман из-за ошибки с авторизацией в сваггере**

http://202.148.53.242:8080/swagger-ui/index.html#