# Лабораторные работы по ООП <img height="32px" alt="emoji" src="https://media.tenor.com/igSFncymgkIAAAAi/4.gif" /> <img height="32px" alt="emoji" src="https://media.tenor.com/gIvmXax4PFcAAAAi/alien-cat.gif" />

Лабораторные работы по объектно-ориентированному программированию на Java.

**Университет:** [Самарский университет](https://ssau.ru/)  
**Язык:** Java  
**Темы:** Функции, интерфейсы, наследование, коллекции, многопоточность  
**Содержание:**

- [Отчеты и таблицы](#отчеты-и-таблицы-)
- [Схемы и диаграммы](#схемы-и-диаграммы-)
- [Документация](#документация-)


## Отчеты и таблицы <img height="75px" alt="emoji" src="https://media.tenor.com/31JlJ6y8viwAAAAi/ep1c.gif" />

### Сравнение производительности
| Операция | [Manual](../../tree/manual) | [Framework](../../tree/framework) | Разница | Ускорение | Операций |
|----------|--------|-----------|-----------|-----------|----------|
| **CREATE** | 4630 ms | 436 ms | -4194 ms | **x10.5** <img height="18px" alt="emoji" src="https://media.tenor.com/KSTj3gSo4CIAAAAi/fire-lit.gif" /> | *10k* |
| **READ** | 650 ms | 559 ms | -91 ms | **x1.16** | *10k* |
| **UPDATE** | 3945 ms | 91 ms | -3854 ms | **x43** <img height="18px" alt="emoji" src="https://media.tenor.com/KSTj3gSo4CIAAAAi/fire-lit.gif" /> | *10k* |
| **DELETE** | 5794 ms | 76 ms | -5718 ms | **x76** <img height="18px" alt="emoji" src="https://media.tenor.com/KSTj3gSo4CIAAAAi/fire-lit.gif" /> | *10k* |
| **SEARCH** | 10302 ms | 6823 ms | -3479 ms | **x1.5** | *1k* |

<img width="175px" height="1px" alt="" src="https://i.imgur.com/qFmcbT0.png" /><img height="75px" alt="emoji" src="https://media.tenor.com/n1-t4E0W_mMAAAAi/cat-chill.gif" />

### Сравнение производительности сортировки
| Операция сортировки | [Manual](../../tree/manual) | [Framework](../../tree/framework) | Разница | Ускорение | Записей |
|---------------------|-------------|-----------|---------|---------|---------|
| Сортировка по имени | 55 ms | 47 ms | -8 ms | **x1.17** | *10k* |
| Сортировка по типу и имени | 54 ms | 14 ms | -40 ms | **x3.85** <img height="18px" alt="emoji" src="https://media.tenor.com/KSTj3gSo4CIAAAAi/fire-lit.gif" /> | *10k* |

<img width="200px" height="1px" alt="" src="https://i.imgur.com/qFmcbT0.png" /><img height="75px" alt="emoji" src="https://media.tenor.com/C4Y-5KEzplAAAAAi/cat-fire.gif" />


<br>

### Отчет о тестовом покрытии

#### Overall Coverage Summary
| Package | Class | Method | Branch | Line |
|---------|-------|--------|--------|------|
| **all classes** |  100% (56/56) <img height="24px" alt="emoji" src="https://media.tenor.com/kwZcJAVUcxkAAAAi/check-yes.gif" /> | 100% (218/218) <img height="24px" alt="emoji" src="https://media.tenor.com/kwZcJAVUcxkAAAAi/check-yes.gif" /> | 100% (250/250) <img height="24px" alt="emoji" src="https://media.tenor.com/kwZcJAVUcxkAAAAi/check-yes.gif" /> | <ins>98.1% (728/742)</ins> <img height="26px" alt="emoji" src="https://media.tenor.com/rlzkgMX4g-wAAAAi/death.gif" /> |

#### Coverage Breakdown
| Package | Class | Method | Branch | Line |
|---------|-------|--------|--------|------|
| **concurrent** | 100% (9/9) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | 100% (30/30) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | 100% (26/26) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | <ins>99% (101/102)</ins> <img height="24px" alt="emoji" src="https://media1.tenor.com/m/Ro_0LtkcR18AAAAC/blue-thing-with-an-eye-looking-shocked-nosolohit.gif" /> |
| **exceptions** | 100% (4/4) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | 100% (8/8) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | - | 100% (8/8) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> |
| **functions** | 100% (18/18) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | 100% (103/103) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | 100% (184/184) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | 100% (339/339) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> |
| **functions.factory** | 100% (3/3) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | 100% (7/7) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | - | 100% (7/7) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> |
| **io** | 100% (9/9) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | 100% (31/31) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | 100% (14/14) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | <ins>93.8% (195/208)</ins> <img height="24px" alt="emoji" src="https://media1.tenor.com/m/H35OFOawLP4AAAAC/hag-monsters-vs-aliens.gif" /> |
| **operations** | 100% (13/13) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | 100% (39/39) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | 100% (26/26) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> | 100% (78/78) <img height="24px" alt="emoji" src="https://media.tenor.com/VBB3xGQXDDgAAAAj/emoji-vergonha1123.gif" /> |

*Generated on 2025-10-19*


<br>

### Итоговая производительность API
| Запрос к конечной точке API | [Manual](../../tree/manual) среднее время (мс) | [Framework](../../tree/framework) среднее время (мс) | Преимущество [Framework](../../tree/framework) |
|----------------|-----------------|--------------------|------------------|
| Add Functions | 12 | 8 | x1.50 |
| Calculate Function Value | 8 | 11 | <ins>x0.73</ins> <img height="18px" alt="emoji" src="https://media.tenor.com/kOwEHJC4SdMAAAAi/loading-small.gif" /> |
| Create Composite Function | 58 | 26 | **x2.23** <img height="18px" alt="emoji" src="https://media.tenor.com/GoMzedSeaQIAAAAi/adamjk-emojis.gif" /> |
| Create Function | 53 | 25 | **x2.12** <img height="18px" alt="emoji" src="https://media.tenor.com/GoMzedSeaQIAAAAi/adamjk-emojis.gif" /> |
| Create Point | 15 | 24 | <ins>x0.62</ins> <img height="18px" alt="emoji" src="https://media.tenor.com/kOwEHJC4SdMAAAAi/loading-small.gif" />  |
| Create User | 62 | 45 | x1.38 |
| Delete Composite Function | 40 | 10 | **x4.00** <img height="18px" alt="emoji" src="https://media.tenor.com/uW2byISIYCUAAAAi/joia-llegal-beleza.gif" /> |
| Delete Function | 52 | 33 | x1.58 |
| Delete Point | 18 | 12 | x1.50 |
| Delete User | 46 | 26 | x1.77 |
| Differentiate Function - LEFT | 11 | 11 | <ins>x1.00</ins> <img height="18px" alt="emoji" src="https://media.tenor.com/kOwEHJC4SdMAAAAi/loading-small.gif" /> |
| Differentiate Function - MIDDLE | 10 | 12 | <ins>x0.83</ins> <img height="18px" alt="emoji" src="https://media.tenor.com/kOwEHJC4SdMAAAAi/loading-small.gif" /> |
| Differentiate Function - RIGHT | 12 | 11 | x1.09 |
| Divide Functions | 13 | 8 | x1.62 |
| Get All Functions | 44 | 9 | **x4.89** <img height="18px" alt="emoji" src="https://media.tenor.com/uW2byISIYCUAAAAi/joia-llegal-beleza.gif" /> |
| Get All Users | 88 | 12 | **x7.33** <img height="18px" alt="emoji" src="https://media.tenor.com/uW2byISIYCUAAAAi/joia-llegal-beleza.gif" /> |
| Get Composite Function By ID | 40 | 8 | **x5.00** <img height="18px" alt="emoji" src="https://media.tenor.com/uW2byISIYCUAAAAi/joia-llegal-beleza.gif" /> |
| Get Composite Functions By User ID | 38 | 10 | **x3.80** <img height="18px" alt="emoji" src="https://media.tenor.com/GoMzedSeaQIAAAAi/adamjk-emojis.gif" /> |
| Get Function By ID | 43 | 9 | **x4.78** <img height="18px" alt="emoji" src="https://media.tenor.com/uW2byISIYCUAAAAi/joia-llegal-beleza.gif" /> |
| Get Functions By User ID | 40 | 12 | **x3.33** <img height="18px" alt="emoji" src="https://media.tenor.com/GoMzedSeaQIAAAAi/adamjk-emojis.gif" /> |
| Get Functions Sorted by Name ASC | 70 | 7 | **x10.00** <img height="18px" alt="emoji" src="https://media.tenor.com/2jiJSKfo6ykAAAAj/amplify-amplifyart.gif" /> |
| Get Functions Sorted by Name DESC | 46 | 8 | **x5.75** <img height="18px" alt="emoji" src="https://media.tenor.com/uW2byISIYCUAAAAi/joia-llegal-beleza.gif" /> |
| Get Functions Sorted by Type and Name | 52 | 8 | **x6.50** <img height="18px" alt="emoji" src="https://media.tenor.com/uW2byISIYCUAAAAi/joia-llegal-beleza.gif" /> |
| Get Functions Sorted by X From ASC | 43 | 8 | **x5.38** <img height="18px" alt="emoji" src="https://media.tenor.com/uW2byISIYCUAAAAi/joia-llegal-beleza.gif" /> |
| Get Point By ID | 41 | 9 | **x4.56** <img height="18px" alt="emoji" src="https://media.tenor.com/uW2byISIYCUAAAAi/joia-llegal-beleza.gif" /> |
| Get Points By Function ID | 39 | 9 | **x4.33** <img height="18px" alt="emoji" src="https://media.tenor.com/uW2byISIYCUAAAAi/joia-llegal-beleza.gif" /> |
| Get User By ID | 55 | 8 | **x6.88** <img height="18px" alt="emoji" src="https://media.tenor.com/uW2byISIYCUAAAAi/joia-llegal-beleza.gif" /> |
| Multiply Functions | 14 | 8 | x1.75 |
| Search Functions - Deep | 41 | 8 | **x5.12** <img height="18px" alt="emoji" src="https://media.tenor.com/uW2byISIYCUAAAAi/joia-llegal-beleza.gif" /> |
| Search Functions - Quick | 57 | 9 | **x6.33** <img height="18px" alt="emoji" src="https://media.tenor.com/uW2byISIYCUAAAAi/joia-llegal-beleza.gif" /> |
| Subtract Functions | 11 | 8 | **x1.38** <img height="18px" alt="emoji" src="https://media.tenor.com/GoMzedSeaQIAAAAi/adamjk-emojis.gif" /> |
| Update Composite Function | 46 | 19 | **x2.42** <img height="18px" alt="emoji" src="https://media.tenor.com/GoMzedSeaQIAAAAi/adamjk-emojis.gif" /> |
| Update Function | 38 | 21 | x1.81 |
| Update Point | 25 | 12 | **x2.08** <img height="18px" alt="emoji" src="https://media.tenor.com/GoMzedSeaQIAAAAi/adamjk-emojis.gif" /> |
| Update User | 52 | 15 | **x3.47** <img height="18px" alt="emoji" src="https://media.tenor.com/GoMzedSeaQIAAAAi/adamjk-emojis.gif" /> |

<img width="300px" height="1px" alt="" src="https://i.imgur.com/qFmcbT0.png" /><img height="150px" alt="emoji" src="https://media.tenor.com/xWphETwf_40AAAAi/kitty-cult-fire.gif" />

<br>

## Схемы и диаграммы <img height="65px" alt="emoji" src="https://media.tenor.com/4CfIDNeonQwAAAAi/postgres.gif" />

### ER диаграмма
```mermaid
erDiagram
    users {
        INT user_id PK "Уникальный ID"
        VARCHAR username "Логин"
        VARCHAR password "Пароль"
        VARCHAR email "Email"
        TIMESTAMP created_at "Дата регистрации"
    }

    functions {
        INT function_id PK "Уникальный ID"
        INT user_id FK "Владелец"
        VARCHAR function_name "Название функции"
        VARCHAR function_type "Тип функции"
        TEXT function_expression "Формула"
        DECIMAL x_from "Начало диапазона"
        DECIMAL x_to "Конец диапазона"
        TIMESTAMP created_at "Дата создания"
    }

    composite_functions {
        INT composite_id PK "Уникальный ID"
        INT user_id FK "Владелец"
        INT first_function_id FK "Первая функция"
        INT second_function_id FK "Вторая функция"
        VARCHAR composite_name "Название композиции"
        TIMESTAMP created_at "Дата создания"
    }

    computed_points {
        INT point_id PK "Уникальный ID"
        INT function_id FK "Функция"
        DECIMAL x_value "Координата X"
        DECIMAL y_value "Координата Y"
        TIMESTAMP computed_at "Дата вычисления"
    }

    users ||--o{ functions : "имеет много"
    users ||--o{ composite_functions : "имеет много"
    functions ||--o{ computed_points : "имеет много"
    functions ||--o{ composite_functions : "используется как первая"
    functions ||--o{ composite_functions : "используется как вторая"
```

### Файлы диаграмм
- Entity-relationship diagram - [ER.puml](ER.puml) <img width="10px" alt="same as above" src="https://media.tenor.com/MUBfaK_k9O8AAAAi/habbo-habbohotel.gif" />
- Sequence diagram for authentication - [SD_AUTH.puml](SD_AUTH.puml) <img width="10px" alt="same as above" src="https://media.tenor.com/KMvIN3R__IkAAAAi/%E9%97%AA%E4%BA%AE.gif" />
- Sequence diagram for calculations - [SD_CACL.puml](SD_CACL.puml) <img width="10px" alt="same as above" src="https://media.tenor.com/KMvIN3R__IkAAAAi/%E9%97%AA%E4%BA%AE.gif" />


<br>

## Документация <img height="32px" alt="Tenor Gif" src="https://media.tenor.com/a2m-Y3dLmD0AAAAi/mona-github-loading-github.gif" />


### Единый API контракт <img width="10px" alt="" src="https://i.imgur.com/qFmcbT0.png" /> <img height="50px" alt="emoji" src="https://media.tenor.com/GpjpezX2PR0AAAAi/books.gif" />
Для обоих веток реализации - [Framework](../../tree/framework) и [Manual](../../tree/manual).

| Конечная точка | Описание |
|----------------|----------|
| `/api/auth/register` <img height="18px" alt="emoji" src="https://media.tenor.com/pQ6Mxpz69vkAAAAi/aklo-ordinals.gif" /> | Создать пользователя (без ролей). |
| `/api/auth/assign-roles` <img height="18px" alt="emoji" src="https://media.tenor.com/l9QWg86fzOUAAAAi/asd.gif" /> | Выдать роли существующему пользователю. |
| `/api/users` <img height="18px" alt="emoji" src="https://media.tenor.com/dmLTEf-u-KoAAAAj/discord-mod.gif" /> | Список пользователей. |
| `/api/users/{id}` <img height="18px" alt="emoji" src="https://media.tenor.com/BsB2DmZZxq4AAAAi/aklo-ordinals.gif" /> | Прочитать, обновить или удалить конкретного пользователя. |
| `/api/users/{id}/functions` <img height="18px" alt="emoji" src="https://media.tenor.com/BsB2DmZZxq4AAAAi/aklo-ordinals.gif" /> | Получить функции владельца. |
| `/api/users/{id}/composite-functions` <img height="18px" alt="emoji" src="https://media.tenor.com/BsB2DmZZxq4AAAAi/aklo-ordinals.gif" /> | Получить составные функции владельца. |
| `/api/functions` <img height="18px" alt="emoji" src="https://media.tenor.com/fv45doRJoWgAAAAj/discord-nitro-op.gif" /> | Список функций (поддерживает сортировку) и создание. |
| `/api/functions/{id}` <img height="18px" alt="emoji" src="https://media.tenor.com/GbTzlneuri0AAAAi/boost-discord.gif" /> | Работа с конкретной функцией. |
| `/api/functions/{id}/calculate` <img height="18px" alt="emoji" src="https://media.tenor.com/nle4lSQc-bQAAAAi/emojify.gif" /> | Посчитать значение функции по точке. |
| `/api/functions/{id}/differentiate` <img height="18px" alt="emoji" src="https://media.tenor.com/nle4lSQc-bQAAAAi/emojify.gif" /> | Посчитать производную (тип передаётся параметром). |
| `/api/functions/{id}/points` <img height="18px" alt="emoji" src="https://media.tenor.com/PgHG_GnGQcMAAAAj/asd.gif" /> | Список точек функции / создание точки. |
| `/api/functions/operations` <img height="18px" alt="emoji" src="https://media.tenor.com/nle4lSQc-bQAAAAi/emojify.gif" /> | Сложение/вычитание/умножение/деление табличных функций. |
| `/api/functions/search` <img height="18px" alt="emoji" src="https://media.tenor.com/nle4lSQc-bQAAAAi/emojify.gif" /> | Поиск функций по тексту, сортировка, пагинация. |
| `/api/composite-functions` <img height="18px" alt="emoji" src="https://media.tenor.com/8wZtCA9STgEAAAAi/asd.gif" /> | Список и создание составных функций. |
| `/api/composite-functions/{id}` <img height="18px" alt="emoji" src="https://media.tenor.com/8wZtCA9STgEAAAAi/asd.gif" /> | CRUD по конкретной составной функции. |
| `/api/points/{id}` <img height="18px" alt="emoji" src="https://media.tenor.com/4I0p7iG0usYAAAAi/rules.gif" /> | Работа с конкретной точкой. |
| `/api/roles` <img height="18px" alt="emoji" src="https://media.tenor.com/oJ_WNeH1PoEAAAAi/discord-mod-discord-bot.gif" /> | Список ролей и создание новой. |
| `/api/roles/{id}` <img height="18px" alt="emoji" src="https://media.tenor.com/oJ_WNeH1PoEAAAAi/discord-mod-discord-bot.gif" /> | Работа с конкретной ролью. |

#### API
- Базовый URL: http://localhost:8080.
- Авторизация: логин + пароль пользователя, созданного через `/api/auth/register` + `/api/auth/assign-roles`.
- Основные ресурсы: `/api/auth`, `/api/users`, `/api/functions`, `/api/points`, `/api/composite-functions`, `/api/roles`.

<br>

### Предварительные требования <img width="10px" alt="" src="https://i.imgur.com/qFmcbT0.png" /><img height="50px" alt="emoji" src="https://media.tenor.com/APUoW9dDiPsAAAAi/clippy-head-scratch.gif" />
- Java 17 и Maven 3.9+.
- PostgreSQL 16.1+.
- Docker Desktop и Git.
- Node.js 18+ и Newman: `npm install -g newman`.

#### Дополнительные требования для [Manual](../../tree/manual):
- Tomcat: apache-tomcat-9.0.112.


<br>

### Установка и запуск <img width="10px" alt="" src="https://i.imgur.com/qFmcbT0.png" /> <img height="50px" alt="emoji" src="https://media.tenor.com/UaBq8N2Z46wAAAAi/add-disc-pc.gif" />
1. **Клонирование репозитория:**

   ```bash
   git clone https://github.com/barinovadn/labs-OOP
   cd labs-OOP
   ```
2. **Выбор ветки:**
   
   Выберите одну из реализаций:
   ```bash
   git checkout manual # Servlets & Tomcat
   ```
   ```bash
   git checkout framework # Spring Boot
   ```
3. **Сборка и запуск:**

   Откройте PowerShell в корне репозитория и выполните:
   ```powershell
   .\docker-run.ps1
   ```
   Скрипт автоматически соберет и запустит проект в Docker контейнере.<br>

4. **Проверка:**

   В том же окне:
   ```powershell
   .\docker-test.ps1
   ```
   Или по http://localhost:8080/api/users - `admin` `admin`.


<br>

### Тесты <img width="10px" alt="" src="https://i.imgur.com/qFmcbT0.png" /> <img height="50px" alt="emoji" src="https://media.tenor.com/V4_qWWNPQnQAAAAi/floor.gif" />
- [docker-test.ps1](../manual/docker-test.ps1) | [docker-test.ps1](../framework/docker-test.ps1) - Упрощенные API тесты.
- [src/test/postman](src/test/postman/) - Newman тесты.
    - [run-tests.ps1](../manual/src/test/postman/run-tests.ps1) | [run-tests.ps1](../framework/src/test/postman/run-tests.ps1) - Основные API тесты.
    - [run-performance.ps1](../manual/src/test/postman/run-performance.ps1) | [run-performance.ps1](../framework/src/test/postman/run-performance.ps1) - Тесты для проверки. производительности, [таблица сравнения](#итоговая-производительность-api).
    - [/results/](src/test/postman/results/) - Подробные результаты Newman тестов.
- [src/test/java](src/test/java/) - Java тесты.

### Дополнительно
- Результаты производительности для каждой ветки сохраняются в [src/test/postman/results/PERFORMANCE_RESULTS.md](../manual/src/test/postman/results/PERFORMANCE_RESULTS.md).


<br>

<img width="49.5%" alt="Dasha" src="https://media1.tenor.com/m/ZOfq5Jc-PYgAAAAd/sad-cement.gif" /> <img width="49.5%" alt="Dasha" src="https://media1.tenor.com/m/tZiazDeyfWMAAAAd/he-was-forced-to-use-sql.gif" />
