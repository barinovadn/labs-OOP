# Лабораторные работы по ООП <img height="32px" alt="emoji" src="https://media.tenor.com/igSFncymgkIAAAAi/4.gif" /> <img height="32px" alt="emoji" src="https://media.tenor.com/gIvmXax4PFcAAAAi/alien-cat.gif" />

Лабораторные работы по объектно-ориентированному программированию на Java.

**Университет:** [Самарский университет](https://ssau.ru/)  
**Язык:** Java  
**Темы:** Функции, интерфейсы, наследование, коллекции, многопоточность  
**Содержание:**

- [Отчеты и таблицы](#отчеты-и-таблицы)
- [Схемы и диаграммы](#схемы-и-диаграммы)
- [Документация](#документация)


<br>

## Отчеты и таблицы

### Сравнение производительности

| Операция | [Manual](../../tree/manual) | [Framework](../../tree/framework) | Разница | Ускорение | Операций |
|----------|--------|-----------|-----------|-----------|----------|
| **CREATE** | 4630 ms | 436 ms | -4194 ms | **x10.5** <img height="18px" alt="emoji" src="https://media.tenor.com/5Lfdp0ZDVOQAAAAi/fire.gif" /> | *10k* |
| **READ** | 650 ms | 559 ms | -91 ms | **x1.16** | *10k* |
| **UPDATE** | 3945 ms | 91 ms | -3854 ms | **x43** <img height="18px" alt="emoji" src="https://media.tenor.com/5Lfdp0ZDVOQAAAAi/fire.gif" /> | *10k* |
| **DELETE** | 5794 ms | 76 ms | -5718 ms | **x76** <img height="18px" alt="emoji" src="https://media.tenor.com/5Lfdp0ZDVOQAAAAi/fire.gif" /> | *10k* |
| **SEARCH** | 10302 ms | 6823 ms | -3479 ms | **x1.5** | *1k* |


### Сравнение производительности сортировки

| Операция сортировки | [Manual](../../tree/manual) | [Framework](../../tree/framework) | Разница | Ускорение | Записей |
|---------------------|-------------|-----------|---------|---------|---------|
| Сортировка по имени | 55 ms | 47 ms | -8 ms | **x1.17** | *10k* |
| Сортировка по типу и имени | 54 ms | 14 ms | -40 ms | **x3.85** <img height="18px" alt="emoji" src="https://media.tenor.com/5Lfdp0ZDVOQAAAAi/fire.gif" /> | *10k* |


<br>

### Отчет о тестовом покрытии

#### Overall Coverage Summary
| Package | Class | Method | Branch | Line |
|---------|-------|--------|--------|------|
| all classes |  100% (56/56) <img height="18px" alt="emoji" src="https://media.tenor.com/OIrnk2MXyu0AAAAi/tak.gif" /> | 100% (218/218) <img height="18px" alt="emoji" src="https://media.tenor.com/OIrnk2MXyu0AAAAi/tak.gif" /> | 100% (250/250) <img height="18px" alt="emoji" src="https://media.tenor.com/OIrnk2MXyu0AAAAi/tak.gif" /> | <ins>98.1% (728/742)</ins> <img height="18px" alt="emoji" src="https://media.tenor.com/s_gvo82o5Q4AAAAi/sad-depressed.gif" /> |

#### Coverage Breakdown

| Package | Class | Method | Branch | Line |
|---------|-------|--------|--------|------|
| **concurrent** | 100% (9/9) | 100% (30/30) | 100% (26/26) | <ins>99% (101/102)</ins> |
| **exceptions** | 100% (4/4) | 100% (8/8) | - | 100% (8/8) |
| **functions** | 100% (18/18) | 100% (103/103) | 100% (184/184) | 100% (339/339) |
| **functions.factory** | 100% (3/3) | 100% (7/7) | - | 100% (7/7) |
| **io** | 100% (9/9) | 100% (31/31) | 100% (14/14) | <ins>93.8% (195/208)</ins> |
| **operations** | 100% (13/13) | 100% (39/39) | 100% (26/26) | 100% (78/78) |

*Generated on 2025-10-19*


<br>

### Итоговая производительность API

| Запрос к конечной точке API | [Manual](../../tree/manual) среднее время (мс) | [Framework](../../tree/framework) среднее время (мс) | Преимущество [Framework](../../tree/framework) |
|----------------|-----------------|--------------------|------------------|
| Add Functions | 12 | 8 | x1.50 |
| Calculate Function Value | 8 | 11 | <ins>x0.73</ins> <img height="18px" alt="emoji" src="https://media.tenor.com/kOwEHJC4SdMAAAAi/loading-small.gif" /> |
| Create Composite Function | 58 | 26 | **x2.23** |
| Create Function | 53 | 25 | **x2.12** |
| Create Point | 15 | 24 | <ins>x0.62</ins>  |
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
| Get Composite Functions By User ID | 38 | 10 | **x3.80** |
| Get Function By ID | 43 | 9 | **x4.78** <img height="18px" alt="emoji" src="https://media.tenor.com/uW2byISIYCUAAAAi/joia-llegal-beleza.gif" /> |
| Get Functions By User ID | 40 | 12 | **x3.33** |
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
| Subtract Functions | 11 | 8 | **x1.38** |
| Update Composite Function | 46 | 19 | **x2.42** |
| Update Function | 38 | 21 | x1.81 |
| Update Point | 25 | 12 | **x2.08** |
| Update User | 52 | 15 | **x3.47** |


<br>

## Схемы и диаграммы

<img height="100px" alt="emoji" src="https://media.tenor.com/4CfIDNeonQwAAAAi/postgres.gif" />

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

- Entity-relationship diagram - [ER.puml](ER.puml)
- Sequence diagram for authentication - [SD_AUTH.puml](SD_AUTH.puml)
- Sequence diagram for calculations - [SD_CACL.puml](SD_CACL.puml)


<br>

## Документация <img height="32px" alt="Tenor Gif" src="https://media.tenor.com/a2m-Y3dLmD0AAAAi/mona-github-loading-github.gif" />

    . . .

<br>

<img width="49.5%" alt="Dasha" src="https://media1.tenor.com/m/ZOfq5Jc-PYgAAAAd/sad-cement.gif" /> <img width="49.5%" alt="Dasha" src="https://media1.tenor.com/m/tZiazDeyfWMAAAAd/he-was-forced-to-use-sql.gif" />
