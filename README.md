# Лабораторные работы по ООП

Лабораторные работы по объектно-ориентированному программированию на Java.

**Университет:** [Самарский университет](https://ssau.ru/)   
**Язык:** Java  
**Темы:** функции, интерфейсы, наследование, коллекции, многопоточность, синхронизация

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

### Отчет о тестовом покрытии

#### Overall Coverage Summary
| Package | Class | Method | Branch | Line |
|---------|-------|--------|--------|------|
| all classes | 100% (56/56) | 100% (218/218) | 100% (250/250) | 98.1% (728/742) |

#### Coverage Breakdown
| Package | Class | Method | Branch | Line |
|---------|-------|--------|--------|------|
| **concurrent** | 100% (9/9) | 100% (30/30) | 100% (26/26) | 99% (101/102) |
| **exceptions** | 100% (4/4) | 100% (8/8) | - | 100% (8/8) |
| **functions** | 100% (18/18) | 100% (103/103) | 100% (184/184) | 100% (339/339) |
| **functions.factory** | 100% (3/3) | 100% (7/7) | - | 100% (7/7) |
| **io** | 100% (9/9) | 100% (31/31) | 100% (14/14) | 93.8% (195/208) |
| **operations** | 100% (13/13) | 100% (39/39) | 100% (26/26) | 100% (78/78) |

*Generated on 2025-10-19*
<br>

<img width="50%" alt="sad-cement" src="https://media1.tenor.com/m/ZOfq5Jc-PYgAAAAd/sad-cement.gif" /> <img width="49%" alt="sad-cement" src="https://media1.tenor.com/m/tZiazDeyfWMAAAAd/he-was-forced-to-use-sql.gif" />
