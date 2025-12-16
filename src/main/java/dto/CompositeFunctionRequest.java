package dto;

public class CompositeFunctionRequest {
    private Long userId;
    private String compositeName;
    private Long firstFunctionId;
    private Long secondFunctionId;
    private Double xFrom;
    private Double xTo;
    private String operationMethod;

    public CompositeFunctionRequest() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCompositeName() { return compositeName; }
    public void setCompositeName(String compositeName) { this.compositeName = compositeName; }

    public Long getFirstFunctionId() { return firstFunctionId; }
    public void setFirstFunctionId(Long firstFunctionId) { this.firstFunctionId = firstFunctionId; }

    public Long getSecondFunctionId() { return secondFunctionId; }
    public void setSecondFunctionId(Long secondFunctionId) { this.secondFunctionId = secondFunctionId; }

    public Double getXFrom() { return xFrom; }
    public void setXFrom(Double xFrom) { this.xFrom = xFrom; }

    public Double getXTo() { return xTo; }
    public void setXTo(Double xTo) { this.xTo = xTo; }

    public String getOperationMethod() { return operationMethod; }
    public void setOperationMethod(String operationMethod) { this.operationMethod = operationMethod; }
}

