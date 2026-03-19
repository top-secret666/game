# Доменная модель бэкенда (Stage 1: монолит)

Цель: поддержать игровой цикл «смена», кейсы, документы и проверки.

## Основные сущности
- Player
  - id, email/username, createdAt
  - profile: displayName, preferences

- Case (обращение/кейс)
  - id, status (DRAFT/IN_REVIEW/FINAL)
  - patientAlias (псевдоним), ageRange (не точный возраст)
  - createdBy (playerId)
  - flags: confidentialityLevel

- SessionNote (заметка по разговору)
  - caseId
  - textSections (структурированные блоки)
  - createdAt

- FormAnswer (анкета)
  - caseId
  - fieldKey, value

- SupportPlan (план поддержки)
  - caseId
  - goals (short)
  - accommodations (например «тихая комната»)
  - followUp (дата/тип)

- Document (отчёт/протокол)
  - caseId
  - type (SHIFT_REPORT/CONSENT/NOTE/PLAN)
  - version, content
  - signedAt (если есть)

- AuditCheck (проверка начальства)
  - caseId
  - checklistResults
  - score
  - remarks

- WardUpgrade (улучшение отделения)
  - playerId
  - type
  - level

## Ключевые сценарии API (MVP)
- Создать кейс
- Получить кейс (с заметками/документами)
- Сохранить ответы анкеты
- Сохранить заметку
- Сгенерировать/сохранить документ
- Отправить кейс на проверку
- Получить результат проверки
- Список улучшений отделения

## Системные требования (будущие квесты)
- Security: JWT + роли (DOCTOR/SUPERVISOR)
- Audit log: кто и когда смотрел кейс
- Eventing: событие «case.finalized»
- Cache: справочники полей/шаблоны документов
