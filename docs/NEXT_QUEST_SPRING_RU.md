# Следующий квест (Spring/Java): «Очаг Проекта» → «Кейс приёма»

## Что ты прокачиваешь
- Spring Boot: controller/service/repository
- JPA + миграции
- DTO + validation
- Обработка ошибок

## Требования (MVP)
### 1) Проект
- Монолит Spring Boot (Maven или Gradle)
- Зависимости: Spring Web, Validation, Spring Data JPA, PostgreSQL, Flyway
- Локальный запуск с Postgres

### 2) Данные
Минимальные таблицы:
- `cases`
  - `id` bigserial PK
  - `status` varchar (DRAFT/FINAL)
  - `patient_alias` varchar
  - `age_range` varchar (например "18-25", "26-35")
  - `confidentiality_level` varchar (LOW/MEDIUM/HIGH)
  - `created_at` timestamptz

> Важно: это художественные поля; мы не храним чувствительные медицинские детали.

### 3) API
- `POST /api/cases`
  - Request DTO: `patientAlias`, `ageRange`, `confidentialityLevel`
  - Response DTO: `id`, все поля, `status=DRAFT`, `createdAt`
  - Валидация: строки не пустые, длина ограничена

- `GET /api/cases/{id}`
  - Возвращает Case DTO
  - Если не найдено: 404 с понятным телом ошибки

## Definition of Done
- Flyway миграция создаёт таблицу `cases`
- Контроллер не возвращает entity напрямую
- Валидация работает (400)
- Ошибки оформлены единообразно

## Подсказки (не решение)
- Слои: `CaseController` → `CaseService` → `CaseRepository`
- В service: минимум логики (создать DRAFT, сохранить)
- Для ошибок: `@ControllerAdvice` + свой `ApiError`
