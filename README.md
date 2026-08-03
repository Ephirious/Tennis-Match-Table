# Tennis Match Table


## Стек проекта
- Spring MVC
- Apache Maven
- JPA\Hibernate ORM
- PostgreSQL
- Docker


## Цель проекта
1. Ознакомится с частью фреймворка Spring: Spring MVC
2. Изучить методы работы с ORM Hibernate в связке с JPA
3. Изучить архитектурные решения, используемые при создание архитектуры по методологии DDD

## API приложения

###  Общий формат ошибок
Для всех запросов в случае ошибки ответ имеет статус **400 Bad Request** или **404 Not Found** и выглядит следующим образом:

```json
{
  "message": "Имена игроков не могут совпадать"
}
```
> 💡 *Значение поля `message` динамически меняется в зависимости от того, какая именно ошибка произошла.*

---

### 1. Создание нового матча

*   **Метод:** `POST`
*   **Эндпоинт:** `/matches`
*   **Content-Type:** `application/json`

#### Тело запроса
```json
{
  "firstPlayerName": "First Player",
  "secondPlayerName": "Second Player"
}
```

#### Ответ в случае успеха
*   **Статус:** `201 Created`
```json
{
  "id": "1d5e5fb4-5203-4933-8278-486f3d8db2ca"
}
```

#### Коды ошибок
*   `400 Bad Request` — Ошибки валидации (например, если переданы одинаковые имена).

---

### 2. Начисление очков и получение счёта

*   **Метод:** `POST`
*   **Эндпоинт:** `/matches/{uuid}/point`
*   **Content-Type:** `application/json`

#### Параметры пути (Path)
*   `uuid` (строка) — Уникальный идентификатор матча.

#### Тело запроса
```json
{
  "name": "First Player"
}
```

#### Ответ в случае успеха
*   **Статус:** `200 OK`

<details>
<summary><b>Вариант 1: Обычный гейм (нажмите, чтобы развернуть)</b></summary>
<i>В обычном гейме поле <code>tieBreakPoints</code> равно <code>null</code> или отсутствует.</i>

```json
{
  "firstPlayer": {
    "name": "First Player",
    "points": "40",
    "games": 2,
    "sets": 0,
    "tieBreakPoints": null
  },
  "secondPlayer": {
    "name": "Second Player",
    "points": "AD",
    "games": 3,
    "sets": 1,
    "tieBreakPoints": null
  },
  "winnerName": null
}
```
</details>

<details>
<summary><b>Вариант 2: Тай-брейк (нажмите, чтобы развернуть)</b></summary>
<i>В тай-брейке поле <code>points</code> равно <code>null</code> или отсутствует.</i>

```json
{
  "firstPlayer": {
    "name": "First Player",
    "points": null,
    "games": 6,
    "sets": 0,
    "tieBreakPoints": 5
  },
  "secondPlayer": {
    "name": "Second Player",
    "points": null,
    "games": 6,
    "sets": 0,
    "tieBreakPoints": 4
  },
  "winnerName": null
}
```
</details>

#### Коды ошибок
*   `404 Not Found` — Матч с указанным `uuid` не найден в системе.

---

### 3. Получение текущего счёта матча

*   **Метод:** `GET`
*   **Эндпоинт:** `/matches/{uuid}`

#### Параметры пути (Path)
*   `uuid` (строка) — Уникальный идентификатор матча.

#### Ответ в случае успеха
*   **Статус:** `200 OK`

<details>
<summary><b>Вариант 1: Обычный гейм (нажмите, чтобы развернуть)</b></summary>

```json
{
  "firstPlayer": {
    "name": "First Player",
    "points": "40",
    "games": 2,
    "sets": 0,
    "tieBreakPoints": null
  },
  "secondPlayer": {
    "name": "Second Player",
    "points": "AD",
    "games": 3,
    "sets": 1,
    "tieBreakPoints": null
  },
  "winnerName": null
}
```
</details>

<details>
<summary><b>Вариант 2: Тай-брейк (нажмите, чтобы развернуть)</b></summary>

```json
{
  "firstPlayer": {
    "name": "First Player",
    "points": null,
    "games": 6,
    "sets": 0,
    "tieBreakPoints": 5
  },
  "secondPlayer": {
    "name": "Second Player",
    "points": null,
    "games": 6,
    "sets": 0,
    "tieBreakPoints": 4
  },
  "winnerName": null
}
```
</details>

#### Коды ошибок
*   `404 Not Found` — Матч с указанным `uuid` не найден в системе.

---

### 4. Список завершённых матчей

*   **Метод:** `GET`
*   **Эндпоинт:** `/matches`

#### Параметры запроса (Query)
*   `page` (число, необязательный) — Номер страницы для пагинации.
*   `player_name` (строка, необязательный) — Фильтр по имени игрока.

#### Ответ в случае успеха
*   **Статус:** `200 OK`
```json
{
  "matches": [
    {
      "firstPlayerName": "First Player",
      "secondPlayerName": "Second Player",
      "winnerName": "Second Player"
    },
    {
      "firstPlayerName": "First Player",
      "secondPlayerName": "Second Player",
      "winnerName": "First Player"
    }
  ],
  "currentPage": 0,
  "totalPages": 10
}
```



## Архитектура доменных моделей
* **backend/src/main/java/com/ephirious/model**
    * 📂 **aggregate** (Агрегаты)
        * 📄 `Match.java`
        * 📄 `MatchType.java`
    * 📂 **entity** (Сущности)
        * 📄 `Player.java`
    * 📂 **value** (Value Objects)
        * 📂 **player**
            * 📄 `PlayerName.java`
        * 📂 **score** (Система счета)
            * 📄 `PlayerSide.java`
            * 📄 `Score.java`
            * 📂 **game** (Гейм)
                * 📄 `AbstractGameScore.java`
                * 📄 `StandardGameScore.java`
                * 📄 `TieBreakGameScore.java`
                * 📄 `FinalStandardGameScore.java`
                * 📄 `StandardPointState.java`
                * 📄 `FinalGameState.java`
                * 📄 `TieBreakGameType.java`
            * 📂 **set** (Сет)
                * 📄 `AbstractSetScore.java`
                * 📄 `StandardSetScore.java`
                * 📄 `BigSetScore.java`
            * 📂 **match** (Матч)
                * 📄 `AbstractMatchScore.java`
                * 📄 `ThreeSetMatchScore.java`
                * 📄 `FiveSetMatchScore.java`


## Запуск приложения
Для сборки необходимо сделать следующие действия:
1. Установить Docker & Docker Compose
2. Переименовать файл `.env.example` -> `.env` и заполнить заданные переменные:
    - `EXTERNAL_TOMCAT_PORT` - порт, по которому будет доступна Backend-часть приложения
    - `DATABASE_NAME` - имя базы данных
    - `DATABASE_USER` - имя пользователя БД
    - `DATABASE_PASSWORD` - пароль БД
    - `DATABASE_PROTOCOL` - протокол БД для формирования URL для JDBC
    - `DATABASE_PORT` - порт БД в Docker-сети (при использовании PostgreSQL менять необязательно)
    - `DATABASE_SERVER` - доменное имя, по которому происходит подключение к БД (менять не требуется). Зависит от имени контейнера БД в файле описания docker-compose
    - `DATABASE_DRIVER` - название класса-драйвера БД для автоматической загрузки
    - `EXTERNAL_DATABASE_PORT` - внешний порт к БД
    - `FRONTEND_PORT` - порт, которому будет доступен Frontend
3. Использовать следующие команды:
    - `docker compose up --build` - сборка и запуск проекта
    - `docker compose down` - остановка работы проекта
    - `docker compose down -v` - остановка работы проекта с удалением содержимого БД
4. Использовать API приложения по следующему адресу `http://localhost:{FRONTEND_PORT}/api/`
5. Использовать приложение с фронтэндом по следующему адресу `http://localhost:{FRONTEND_PORT}/"


