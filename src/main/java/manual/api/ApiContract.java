package manual.api;

import manual.dto.*;

import java.util.List;

public interface ApiContract {
    
    // USERS
    
    // Создать нового пользователя
    // POST /api/users
    ApiResponse<UserResponse> createUser(CreateUserRequest request);
    
    // Получить пользователя по ID
    // GET /api/users/{id}
    ApiResponse<UserResponse> getUserById(Long userId);
    
    // Получить всех пользователей
    // GET /api/users
    ApiResponse<List<UserResponse>> getAllUsers();
    
    // Обновить пользователя
    // PUT /api/users/{id}
    ApiResponse<UserResponse> updateUser(Long userId, CreateUserRequest request);
    
    // Удалить пользователя
    // DELETE /api/users/{id}
    ApiResponse<Void> deleteUser(Long userId);
    
    
    // FUNCTIONS
    
    // Создать новую функцию
    // POST /api/functions
    ApiResponse<FunctionResponse> createFunction(CreateFunctionRequest request, Long userId);
    
    // Получить функцию по ID
    // GET /api/functions/{id}
    ApiResponse<FunctionResponse> getFunctionById(Long functionId);
    
    // Получить все функции пользователя
    // GET /api/users/{userId}/functions
    ApiResponse<List<FunctionResponse>> getFunctionsByUserId(Long userId);
    
    // Получить все функции
    // GET /api/functions
    ApiResponse<List<FunctionResponse>> getAllFunctions();
    
    // Получить все функции, отсортированные по имени (ASC)
    // GET /api/functions?sort=name_asc
    ApiResponse<List<FunctionResponse>> getAllFunctionsSortedByNameAsc();
    
    // Получить все функции, отсортированные по имени (DESC)
    // GET /api/functions?sort=name_desc
    ApiResponse<List<FunctionResponse>> getAllFunctionsSortedByNameDesc();
    
    // Получить все функции, отсортированные по x_from (ASC)
    // GET /api/functions?sort=x_from_asc
    ApiResponse<List<FunctionResponse>> getAllFunctionsSortedByXFromAsc();
    
    // Получить все функции, отсортированные по типу и имени
    // GET /api/functions?sort=type_name
    ApiResponse<List<FunctionResponse>> getAllFunctionsSortedByTypeAndName();
    
    // Обновить функцию
    // PUT /api/functions/{id}
    ApiResponse<FunctionResponse> updateFunction(Long functionId, CreateFunctionRequest request);
    
    // Удалить функцию
    // DELETE /api/functions/{id}
    ApiResponse<Void> deleteFunction(Long functionId);
    
    
    // POINTS
    
    // Создать точку для функции
    // POST /api/functions/{functionId}/points
    ApiResponse<PointResponse> createPoint(Long functionId, CreatePointRequest request);
    
    // Получить точку по ID
    // GET /api/points/{id}
    ApiResponse<PointResponse> getPointById(Long pointId);
    
    // Получить все точки функции
    // GET /api/functions/{functionId}/points
    ApiResponse<List<PointResponse>> getPointsByFunctionId(Long functionId);
    
    // Обновить точку
    // PUT /api/points/{id}
    ApiResponse<PointResponse> updatePoint(Long pointId, CreatePointRequest request);
    
    // Удалить точку
    // DELETE /api/points/{id}
    ApiResponse<Void> deletePoint(Long pointId);
    
    // Вычислить значение функции в точке x
    // Сначала проверяет БД, если точки нет - вычисляет и сохраняет
    // POST /api/functions/{functionId}/calculate
    ApiResponse<PointResponse> calculateFunctionValue(Long functionId, Double x);
    
    
    // COMPOSITE FUNCTIONS
    
    // Создать композитную функцию
    // POST /api/composite-functions
    ApiResponse<CompositeFunctionResponse> createCompositeFunction(
            CreateCompositeFunctionRequest request, Long userId);
    
    // Получить композитную функцию по ID
    // GET /api/composite-functions/{id}
    ApiResponse<CompositeFunctionResponse> getCompositeFunctionById(Long compositeId);
    
    // Получить все композитные функции пользователя
    // GET /api/users/{userId}/composite-functions
    ApiResponse<List<CompositeFunctionResponse>> getCompositeFunctionsByUserId(Long userId);
    
    // Обновить композитную функцию
    // PUT /api/composite-functions/{id}
    ApiResponse<CompositeFunctionResponse> updateCompositeFunction(
            Long compositeId, CreateCompositeFunctionRequest request);
    
    // Удалить композитную функцию
    // DELETE /api/composite-functions/{id}
    ApiResponse<Void> deleteCompositeFunction(Long compositeId);
    
    
    // FUNCTION OPERATIONS
    
    // Выполнить операцию над функциями (add, subtract, multiply, divide)
    // POST /api/functions/operations
    ApiResponse<CalculationResponse> performFunctionOperation(CalculationRequest request);
    
    // Вычислить производную функции
    // POST /api/functions/{functionId}/differentiate
    ApiResponse<CalculationResponse> differentiateFunction(
            Long functionId, String differentialType, Double step);
    
    // Поиск в графе функций
    // POST /api/functions/search
    ApiResponse<List<FunctionResponse>> searchFunctions(
            SearchCriteria criteria, String algorithm);
}

