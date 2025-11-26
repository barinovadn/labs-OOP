# Лабораторные работы по ООП

Лабораторные работы по объектно-ориентированному программированию на Java.

**Университет:** [Самарский университет](https://ssau.ru/)   
**Язык:** Java  
**Темы:** Функции, интерфейсы, наследование, коллекции, многопоточность, синхронизация  
**Доки:** [Полная документация](https://github.com/barinovadn/labs-OOP#документация-)  


<br>

### Предварительные требования <img width="10px" alt="" src="https://i.imgur.com/qFmcbT0.png" /><img height="50px" alt="emoji" src="https://media.tenor.com/APUoW9dDiPsAAAAi/clippy-head-scratch.gif" />
- Java 17 и Maven 3.9+.
- PostgreSQL 16.1+.
- Docker Desktop и Git.
- Node.js 18+ и Newman: `npm install -g newman`.
- Tomcat: apache-tomcat-9.0.112.


<br>

### Установка и запуск <img width="10px" alt="" src="https://i.imgur.com/qFmcbT0.png" /> <img height="50px" alt="emoji" src="https://media.tenor.com/UaBq8N2Z46wAAAAi/add-disc-pc.gif" />
1. **Клонирование репозитория:**

   ```bash
   git clone https://github.com/barinovadn/labs-OOP
   cd labs-OOP
   ```
2. **Выбор ветки:**

   ```bash
   git checkout manual # Servlets & Tomcat
   ```
4. **Сборка и запуск:**

   Откройте PowerShell в корне репозитория и выполните:
   ```powershell
   .\docker-run.ps1
   ```
   Скрипт автоматически соберет и запустит проект в Docker контейнере.<br>

5. **Проверка:**

   В том же окне:
   ```powershell
   .\docker-test.ps1
   ```
   Или по http://localhost:8080/api/users - `admin` `admin`.


<br>

### Тесты <img width="10px" alt="" src="https://i.imgur.com/qFmcbT0.png" /> <img height="50px" alt="emoji" src="https://media.tenor.com/V4_qWWNPQnQAAAAi/floor.gif" />
- [docker-test.ps1](docker-test.ps1) - Упрощенные API тесты.
- [src/test/postman](src/test/postman/) - Newman тесты.
    - [run-tests.ps1](src/test/postman/run-tests.ps1) - Основные API тесты.
    - [run-performance.ps1](src/test/postman/run-performance.ps1) - Тесты для проверки. производительности, [таблица сравнения](https://github.com/barinovadn/labs-OOP#итоговая-производительность-api).
    - [/results/](src/test/postman/results/) - Подробные результаты Newman тестов.
- [src/test/java](src/test/java/) - Java тесты.

### Дополнительно
- Результаты производительности для каждой ветки сохраняются в [PERFORMANCE_RESULTS.md](src/test/postman/results/PERFORMANCE_RESULTS.md).
- [Полная документация](https://github.com/barinovadn/labs-OOP#документация-).
