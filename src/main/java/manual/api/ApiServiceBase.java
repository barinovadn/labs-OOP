package manual.api;

import manual.DatabaseConnection;
import manual.dto.CreatePointRequest;
import manual.dto.PointResponse;
import manual.entity.FunctionEntity;
import manual.entity.PointEntity;
import manual.repository.FunctionRepository;
import manual.repository.PointRepository;
import functions.MathFunction;
import functions.factory.TabulatedFunctionFactory;
import functions.factory.ArrayTabulatedFunctionFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public abstract class ApiServiceBase {
    
    private static final Logger logger = Logger.getLogger(ApiServiceBase.class.getName());
    
    protected Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    protected PointResponse calculateFunctionValueWithCache(Long functionId, Double x) 
            throws SQLException {
        logger.info("Вычисление значения функции " + functionId + " в точке x=" + x);
        
        try (Connection conn = getConnection()) {
            PointRepository pointRepo = new PointRepository(conn);
            FunctionRepository functionRepo = new FunctionRepository(conn);
            
            logger.fine("Проверка БД на наличие точки (function_id=" + functionId + ", x=" + x + ")");
            PointResponse existingPoint = pointRepo.findByFunctionIdAndX(functionId, x);
            
            if (existingPoint != null) {
                logger.info("Точка найдена в БД, возвращаем кэшированный результат");
                return existingPoint;
            }
            
            logger.fine("Точка не найдена, получаем функцию из БД");
            manual.dto.FunctionResponse functionResponse = functionRepo.findById(functionId);
            
            if (functionResponse == null) {
                throw new SQLException("Функция с ID " + functionId + " не найдена");
            }
            
            FunctionEntity function = mapToEntity(functionResponse);
            
            logger.info("Вычисление значения функции с выражением: " + function.getFunctionExpression());
            double y = computeFunctionValue(function, x);
            logger.info("Вычислено значение y=" + y + " для x=" + x);
            
            logger.fine("Сохранение новой точки в БД");
            CreatePointRequest pointRequest = new CreatePointRequest();
            pointRequest.setXValue(x);
            pointRequest.setYValue(y);
            
            PointResponse savedPoint = pointRepo.create(pointRequest, function);
            logger.info("Точка сохранена в БД с ID: " + savedPoint.getPointId());
            
            return savedPoint;
        }
    }
    
    private FunctionEntity mapToEntity(manual.dto.FunctionResponse response) {
        FunctionEntity entity = new FunctionEntity();
        entity.setFunctionId(response.getFunctionId());
        entity.setFunctionName(response.getFunctionName());
        entity.setFunctionType(response.getFunctionType());
        entity.setFunctionExpression(response.getFunctionExpression());
        entity.setXFrom(response.getXFrom());
        entity.setXTo(response.getXTo());
        entity.setCreatedAt(response.getCreatedAt());
        return entity;
    }
    
    protected double computeFunctionValue(FunctionEntity function, double x) {
        String expression = function.getFunctionExpression();
        logger.fine("Вычисление выражения: " + expression + " для x=" + x);

        try {
            String evalExpression = expression.replace("x", String.valueOf(x));
            return evaluateExpression(evalExpression);
        } catch (Exception e) {
            logger.severe("Ошибка вычисления выражения: " + e.getMessage());
            throw new RuntimeException("Не удалось вычислить значение функции", e);
        }
    }
    
    private double evaluateExpression(String expression) {
        expression = expression.trim();
        
        if (expression.matches("-?\\d+(\\.\\d+)?")) {
            return Double.parseDouble(expression);
        }
        
        logger.warning("Сложное выражение не поддерживается: " + expression);
        return 0.0;
    }
}

