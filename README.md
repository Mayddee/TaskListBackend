# TaskList Backend Documentation

Welcome to the backend of the **TaskList** application — a Spring Boot-powered REST API that provides task management functionality with robust JWT-based authentication and role-based access control.

---

## 🚀 Features Overview

* ✅ **User Authentication** via JWT (access and refresh tokens)
* 📆 **CRUD operations on Tasks** (Create, Read, Update, Delete)
* 🔒 **Role-based Authorization** with Spring Security
* ⏳ **Token Refresh System** to renew access tokens seamlessly
* ⚖️ **Validation** for all request DTOs using `@Validated`
* 🔄 **OpenAPI (Swagger)** integration for live API testing and documentation

---

## 📂 Project Structure

```
src/main/java/org/example/tasklist
|
|— auth          # Authentication logic, token generation/validation
|— config        # Spring Security + CORS configurations
|— controller    # REST Controllers for users and tasks
|— dto           # Data Transfer Objects (UserDto, TaskDto)
|— mappers       # MapStruct or manual DTO <-> Entity mappers
|— model         # Entity classes (User, Task, Role)
|— repository    # Raw JDBC or JPA repositories
|— service       # Business logic for User and Task domains
```

---

## 📅 How to Run Locally (also preferred to use [Deployed Swagger URL] link below)

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/tasklist-backend.git
cd tasklist-backend
```

### 2. Configure Database

Edit `application.properties` or `application.yml`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/tasklist
db.username=yourusername
db.password=yourpassword
```

### 3. Build and Run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

The app will be available at: `http://localhost:8080`

---

## 🔍 API Reference (Swagger)

Try out all requests directly from Swagger UI:

**🔗 [Deployed Swagger URL](https://tasklistbackend-production-8239.up.railway.app/swagger-ui/index.html)**

Main endpoints:

* `POST /api/v1/auth/register` — Register new user
* `POST /api/v1/auth/login` — Login and receive tokens
* `POST /api/v1/auth/refresh` — Refresh token pair
* `GET /api/v1/tasks` — Get all tasks for the current user
* `POST /api/v1/users/{id}/tasks` — Create new task for user
* so on

---

## 🌎 Deployment

The backend is deployed on **Railway.app** and publicly accessible.
Ensure CORS is configured to allow frontend origins like:

```
http://localhost:3000

```

---

## 🚀 Integration with Frontend

* Frontend can call backend using `axios` or `fetch` with:

  * `Authorization: Bearer <access_token>` in headers
  * Refresh token can be stored in localStorage or cookies
* On `401 Unauthorized`, trigger refresh token flow to renew tokens

---

## ⚙️ Planned Improvements

* ✅ Add email verification during registration
* ✅ Pagination and sorting for task list
* ✅ Enhanced error messages via custom error handler
* ✅ Rate limiting and brute-force protection
* ✅ Admin role with user management

---



---

Made with ❤️ by the Mayddee.
