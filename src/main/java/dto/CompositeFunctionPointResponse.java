package dto;

public class CompositeFunctionPointResponse {
    private Long compositeId;
    private Long userId;
    private Long firstFunctionId;
    private Long secondFunctionId;
    private String operationMethod;

    public CompositeFunctionPointResponse() {}

    public Long getCompositeId() { return compositeId; }
    public void setCompositeId(Long compositeId) { this.compositeId = compositeId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getFirstFunctionId() { return firstFunctionId; }
    public void setFirstFunctionId(Long firstFunctionId) { this.firstFunctionId = firstFunctionId; }

    public Long getSecondFunctionId() { return secondFunctionId; }
    public void setSecondFunctionId(Long secondFunctionId) { this.secondFunctionId = secondFunctionId; }

    public String getOperationMethod() { return operationMethod; }
    public void setOperationMethod(String operationMethod) { this.operationMethod = operationMethod; }
}



