<div align="center">
<img width="1536" height="1024" alt="cb3fbeac-57e9-4442-b08b-f8b873b8d1ec" src="https://github.com/user-attachments/assets/b5ae8054-805f-4bf2-bf37-699a9d8c79e2" />
</div>

# Vitrail Clinic — Game Backend (skeleton)

Это стартовый skeleton Spring Boot проекта для обучения через разработку игры.

Требования:
- Java 17
- Maven
- PostgreSQL (локально)

Настройка БД (пример для psql):

```sql
CREATE USER game WITH PASSWORD 'game';
CREATE DATABASE game OWNER game;
```

Запуск локально (Maven):

```bash
mvn -U clean package
mvn spring-boot:run
```

Конфигурация соединения задаётся через переменные окружения:

| Переменная     | Описание                        | Пример значения                            |
|----------------|---------------------------------|--------------------------------------------|
| `DB_PASSWORD`  | Пароль к БД (**обязательно**)   | `your_secret_password`                     |
| `DB_URL`       | JDBC URL                        | `jdbc:postgresql://localhost:5432/game`    |
| `DB_USERNAME`  | Имя пользователя БД             | `game`                                     |

Пример запуска с передачей переменных:

```bash
DB_PASSWORD=your_secret_password mvn spring-boot:run
```

Или для Windows PowerShell:

```powershell
$env:DB_PASSWORD="your_secret_password"; mvn spring-boot:run
```

API:
- `POST /api/cases` — создать кейс (JSON body `patientAlias`, `ageRange`, `confidentialityLevel`)
- `GET /api/cases/{id}` — получить кейс

Миграции Flyway находятся в `src/main/resources/db/migration`.
