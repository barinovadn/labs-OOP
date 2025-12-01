package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationRequest {
    private String operation; // "add", "subtract", "multiply", "divide"
    
    @JsonProperty("functionAId")
    private Long functionAId;
    
    @JsonProperty("functionBId")
    private Long functionBId;

    public OperationRequest() {}

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public Long getFunctionAId() { return functionAId; }
    public void setFunctionAId(Long functionAId) { this.functionAId = functionAId; }

    public Long getFunctionBId() { return functionBId; }
    public void setFunctionBId(Long functionBId) { this.functionBId = functionBId; }
}

