package dto;

public class FunctionRequest {
    private Long userId;
    private String functionName;
    private String functionType;
    private String functionExpression;
    private Double xFrom;
    private Double xTo;
    private TabulatedFunctionRequest tabulatedFunction;

    public FunctionRequest() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFunctionName() { return functionName; }
    public void setFunctionName(String functionName) { this.functionName = functionName; }

    public String getFunctionType() { return functionType; }
    public void setFunctionType(String functionType) { this.functionType = functionType; }

    public String getFunctionExpression() { return functionExpression; }
    public void setFunctionExpression(String functionExpression) { this.functionExpression = functionExpression; }

    public Double getXFrom() { return xFrom; }
    public void setXFrom(Double xFrom) { this.xFrom = xFrom; }

    public Double getXTo() { return xTo; }
    public void setXTo(Double xTo) { this.xTo = xTo; }

    public TabulatedFunctionRequest getTabulatedFunction() { return tabulatedFunction; }
    public void setTabulatedFunction(TabulatedFunctionRequest tabulatedFunction) { this.tabulatedFunction = tabulatedFunction; }
}

