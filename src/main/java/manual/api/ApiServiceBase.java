package manual.api;

import manual.DatabaseConnection;
import manual.entity.FunctionEntity;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public abstract class ApiServiceBase {
    
    private static final Logger logger = Logger.getLogger(ApiServiceBase.class.getName());
    
    protected Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }
    
    protected double computeFunctionValue(FunctionEntity function, double x) {
        String expression = function.getFunctionExpression();
        logger.fine("Вычисление выражения: " + expression + " для x=" + x);
        String evalExpression = expression.replace("x", String.valueOf(x));
        return evaluateExpression(evalExpression);
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
