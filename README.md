# HMCTS Dev Test Backend

My attempt at developing a backend for a HMCTS case management system.

Run `./gradlew bootRun` to start the service.

## Summary

I have followed the Spring Boot tech stack, and implemented controllers and models for the Case and Tasks. The endpoints allow the frontend to fetch, create, and update tasks, in a strict environment.
Extra computational functions, like sorting and filtering tasks, are also handled by this backend for the benefit of the frontend. The backend also serves as a single source of truth for standards like the task statuses.

For testing, I have implemented unit tests of the controllers. These can be run from VS Code, or alongside a build with `./gradlew build`.

# API Documentation

This backend exposes a REST API for managing **tasks** and **cases**.  
All endpoints return JSON.

---

## Root Endpoint

### `GET /`

- **Description:** Health/welcome endpoint.
- **Response:**
  - `200 OK`
  - Body:
    ```json
    "Welcome to test-backend"
    ```

---

## Task Endpoints

### `GET /api/tasks`

- **Description:** Retrieve all tasks.
- **Response:**
  - `200 OK`
  - Example:
    ```json
    [
      {
        "id": 1,
        "title": "Example Task",
        "description": "Task details",
        "status": "PENDING",
        "dueDateTime": "2025-01-01T10:00:00",
        "caseId": 123
      }
    ]
    ```

---

### `GET /api/tasks/{id}`

- **Description:** Retrieve a task by its ID.
- **Path parameter:**
  - `id` (long) – the ID of the task.
- **Response:**
  - `200 OK` with task object.
  - `404 Not Found` if task doesn’t exist.

---

### `POST /api/tasks`

- **Description:** Create a new task.
- **Request body:**
  ```json
  {
    "title": "New Task",
    "description": "Details about task",
    "status": "PENDING",
    "dueDateTime": "2025-01-01T10:00:00",
    "caseId": 123
  }
  ```
- **Response:**
  - `201 Created`
  - `Location` header points to `/api/tasks/{id}`.
  - Body: task object.

---

### `PUT /api/tasks/{id}`

- **Description:** Update an existing task.
- **Path parameter:**
  - `id` (long) – ID of the task.
- **Request body:**
  ```json
  {
    "title": "Updated Task",
    "description": "New details",
    "status": "IN_PROGRESS",
    "dueDateTime": "2025-02-01T15:00:00"
  }
  ```
- **Response:**
  - `200 OK` with updated task.
  - `404 Not Found` if task doesn’t exist.

---

### `DELETE /api/tasks/{id}`

- **Description:** Delete a task.
- **Path parameter:**
  - `id` (long) – ID of the task.
- **Response:**
  - `204 No Content`

---

## Case Endpoints

### `GET /api/cases/get-example-case`

- **Description:** Returns a hard-coded example case.
- **Response:**
  - `200 OK`
  - Example:
    ```json
    {
      "id": 1,
      "caseReference": "ABC12345",
      "title": "Case Title",
      "description": "Case Description",
      "status": "Case Status",
      "createdAt": "2025-01-01T12:00:00"
    }
    ```

---

### `GET /api/cases/{caseId}/tasks`

- **Description:** Retrieve tasks for a specific case, with optional pagination and filtering.
- **Path parameter:**
  - `caseId` (long) – ID of the case.
- **Query parameters:**
  - `page` (int, default `0`) – page number.
  - `size` (int, default `5`) – page size.
  - `sortBy` (string, default `dueDateTime`) – field to sort by.
  - `direction` (`asc` or `desc`, default `asc`) – sort direction.
  - `statusFilter` (string, default `ANY`) – filter by task status (e.g. `PENDING`, `DONE`).
- **Response:**
  - `200 OK` with a paginated list of tasks.
  - Example:
    ```json
    {
      "content": [
        {
          "id": 1,
          "title": "Example Task",
          "description": "Details",
          "status": "PENDING",
          "dueDateTime": "2025-01-01T10:00:00",
          "caseId": 99
        }
      ],
      "pageable": {
        "pageNumber": 0,
        "pageSize": 5
      },
      "totalElements": 1,
      "totalPages": 1
    }
    ```

---

### `POST /api/cases/{caseId}/tasks`

- **Description:** Create a new task for a case.
- **Path parameter:**
  - `caseId` (long) – ID of the case.
- **Request body:**
  ```json
  {
    "title": "Case task",
    "description": "Details",
    "status": "PENDING",
    "dueDateTime": "2025-03-01T09:00:00"
  }
  ```
- **Response:**
  - `201 Created` with new task.
  - `Location` header points to `/api/tasks/{id}`.
