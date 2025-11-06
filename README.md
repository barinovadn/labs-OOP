# Лабораторные работы по ООП <img height="32px" alt="emoji" src="https://media.tenor.com/igSFncymgkIAAAAi/4.gif" /> <img height="32px" alt="emoji" src="https://media.tenor.com/gIvmXax4PFcAAAAi/alien-cat.gif" />

Лабораторные работы по объектно-ориентированному программированию на Java.

**Университет:** [Самарский университет](https://ssau.ru/)  
**Язык:** Java  
**Темы:** функции, интерфейсы, наследование, коллекции, многопоточность, синхронизация  


<br>

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
| all classes |  100% (56/56) <img height="18px" alt="emoji" src="https://media.tenor.com/OIrnk2MXyu0AAAAi/tak.gif" /> | 100% (218/218) <img height="18px" alt="emoji" src="https://media.tenor.com/OIrnk2MXyu0AAAAi/tak.gif" /> | 100% (250/250) <img height="18px" alt="emoji" src="https://media.tenor.com/OIrnk2MXyu0AAAAi/tak.gif" /> | <ins>98.1% (728/742)</ins>  <img height="18px" alt="emoji" src="https://media.tenor.com/s_gvo82o5Q4AAAAi/sad-depressed.gif" /> |

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

### ER диаграмма

<img height="100px" alt="emoji" src="https://media.tenor.com/4CfIDNeonQwAAAAi/postgres.gif" />

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


<br>

<img width="49.5%" alt="sad-cement" src="https://media1.tenor.com/m/ZOfq5Jc-PYgAAAAd/sad-cement.gif" /> <img width="49.5%" alt="sad-cement" src="https://media1.tenor.com/m/tZiazDeyfWMAAAAd/he-was-forced-to-use-sql.gif" />
