# 📋 TaskList — Backend API

**TaskList** — это RESTful API на Spring Boot, созданное для управления задачами пользователей. Поддерживает аутентификацию, авторизацию, операции с задачами и простую интеграцию с фронтендом.

## 🔗 Swagger (публичная документация API)

📍 **[https://tasklistbackend-production-8239.up.railway.app/swagger-ui/index.html](https://tasklistbackend-production-8239.up.railway.app/swagger-ui/index.html)**

---

## ✨ Возможности (Фичи)

* ✅ Регистрация и вход по JWT
* 🔐 Защищённые эндпоинты
* 🔁 Рефреш access-токена
* 📄 CRUD-действия над задачами
* 🧠 Проверка владельца задачи (авторизация)
* 🔧 SQL-инициализация БД при запуске
* 🌐 Swagger UI с возможностью ручного тестирования
* 🪥 CORS-конфигурация для фронтенда ([http://localhost:3000](http://localhost:3000))

---

## 🚀 Запуск проекта локально

### 1. 📂 Клонировать репозиторий

```bash
git clone https://github.com/Mayddee/TaskListBackend.git
cd tasklist-backend
```

### 2. 📄 Настроить `application.yaml`

Находится в `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tasklist
    username: postgres
    password: your_password
  sql:
    init:
      mode: always
      schema-locations: classpath:schema.sql

security:
  jwt:
    access: 3600000
    refresh: 604800000
    secret: your_secure_secret
```

> ⚡ **Важно:** будет автоматически выполнен `schema.sql`, создающий базу данных при первом запуске.

### 3. 🔍 Убедитесь, что у вас есть PostgreSQL

Создайте базу данных `tasklist` вручную или используйте Railway.

### 4. ▶️ Запустить

```bash
./mvnw spring-boot:run
```

Или запустить `TasklistApplication.java` через IDE (например IntelliJ).

---

## 🤝 Интеграция с фронтендом

* Проект идеально работает с React/Next.js.
* Все защищённые запросы отправляются с заголовком `Authorization: Bearer <accessToken>`.
* Когда access-токен истекает, отправляется запрос на `/auth/refresh` с refresh-токеном.

Пример в React:

```js
axios.get('/api/v1/tasks', {
  headers: { Authorization: `Bearer ${accessToken}` }
});
```

---

## 🎓 Структура проекта

* `schema.sql` — SQL-скрипт с созданием всех таблиц
* `application.yaml` — конфигурация подключения к БД и JWT
* `web.controller` — REST-контроллеры для аутентификации, задач и пользователей
* `service` — бизнес-логика
* `security` — JWT, фильтры, CORS, `JwtTokenProvider`
* `repository` — репозитории, работающие напрямую с базой данных

---

## 📃 Что можно улучшить в будущем?

* ✉️ Подтверждение по email
* 🚀 Мобильная адаптация (через отдельное API или GraphQL)
* ⛏ Добавление unit-тестов (JUnit + Mockito)
* 💡 Улучшенная обработка ошибок
* 🤖 Возможность комментировать задачи и прикреплять файлы

---

## 📽️ Видео-презентация

*(https://www.loom.com/share/7d2afa47e8534eeba5912d3613bc851e?sid=b5855d96-446e-4ca8-a5d0-bdc04eb98ffb)*


