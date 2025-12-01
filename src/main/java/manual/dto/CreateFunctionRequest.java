package manual.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateFunctionRequest {
    private Long userId;
    private String functionName;
    private String functionType;
    private String functionExpression;
    
    @JsonProperty("xFrom")
    private Double xFrom;
    
    @JsonProperty("xTo")
    private Double xTo;

    public CreateFunctionRequest() {}

    public CreateFunctionRequest(Long userId, String functionName, String functionType,
                                 String functionExpression, Double xFrom, Double xTo) {
        this.userId = userId;
        this.functionName = functionName;
        this.functionType = functionType;
        this.functionExpression = functionExpression;
        this.xFrom = xFrom;
        this.xTo = xTo;
    }

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
}