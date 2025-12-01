package manual.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class FunctionResponse {
    private Long functionId;
    private Long userId;
    private String functionName;
    private String functionType;
    private String functionExpression;
    
    @JsonProperty("xFrom")
    private Double xFrom;
    
    @JsonProperty("xTo")
    private Double xTo;
    
    private LocalDateTime createdAt;

    public FunctionResponse() {}

    public FunctionResponse(Long functionId, Long userId, String functionName, String functionType,
                            String functionExpression, Double xFrom, Double xTo, LocalDateTime createdAt) {
        this.functionId = functionId;
        this.userId = userId;
        this.functionName = functionName;
        this.functionType = functionType;
        this.functionExpression = functionExpression;
        this.xFrom = xFrom;
        this.xTo = xTo;
        this.createdAt = createdAt;
    }

    public Long getFunctionId() { return functionId; }
    public void setFunctionId(Long functionId) { this.functionId = functionId; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}