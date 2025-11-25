# Результаты теста производительности

**Branch:** framework  
**Timestamp:** 2025-11-25T08:15:32.451Z  
**Iterations:** 10  

## Производительность конечных точек

| Конечная точка | Средняя (мс) | Минимальная (мс) | Максимальная (мс) | Запросов |
|----------------|--------------|------------------|------------------|----------|
| Create User | 45 | 31 | 72 | 10 |
| Get All Users | 12 | 6 | 20 | 10 |
| Get User By ID | 8 | 5 | 11 | 10 |
| Update User | 15 | 7 | 21 | 10 |
| Delete User | 26 | 20 | 54 | 10 |
| Create Function | 25 | 10 | 35 | 10 |
| Get All Functions | 9 | 5 | 21 | 10 |
| Get Functions Sorted by Name ASC | 7 | 5 | 14 | 10 |
| Get Functions Sorted by Name DESC | 8 | 5 | 13 | 10 |
| Get Functions Sorted by X From ASC | 8 | 6 | 12 | 10 |
| Get Functions Sorted by Type and Name | 8 | 6 | 10 | 10 |
| Get Function By ID | 9 | 5 | 14 | 10 |
| Update Function | 21 | 16 | 28 | 10 |
| Delete Function | 33 | 21 | 48 | 10 |
| Get Functions By User ID | 12 | 7 | 21 | 10 |
| Create Point | 24 | 18 | 32 | 10 |
| Get Points By Function ID | 9 | 7 | 15 | 10 |
| Get Point By ID | 9 | 6 | 11 | 10 |
| Calculate Function Value | 11 | 8 | 16 | 10 |
| Update Point | 12 | 8 | 16 | 10 |
| Delete Point | 12 | 5 | 18 | 10 |
| Create Composite Function | 26 | 19 | 32 | 10 |
| Get Composite Function By ID | 8 | 5 | 13 | 10 |
| Get Composite Functions By User ID | 10 | 7 | 14 | 10 |
| Update Composite Function | 19 | 16 | 23 | 10 |
| Delete Composite Function | 10 | 6 | 15 | 10 |
| Add Functions | 8 | 5 | 13 | 10 |
| Subtract Functions | 8 | 5 | 12 | 10 |
| Multiply Functions | 8 | 5 | 11 | 10 |
| Divide Functions | 8 | 5 | 11 | 10 |
| Differentiate Function - MIDDLE | 12 | 7 | 16 | 10 |
| Differentiate Function - LEFT | 11 | 6 | 15 | 10 |
| Differentiate Function - RIGHT | 11 | 7 | 14 | 10 |
| Search Functions - Quick | 9 | 5 | 16 | 10 |
| Search Functions - Deep | 8 | 5 | 12 | 10 |
