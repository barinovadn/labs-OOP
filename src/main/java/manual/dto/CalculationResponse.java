package manual.dto;

public class CalculationResponse {
    private String operation;
    private Double result;
    private String functionType;
    private Long computationTimeMs;
    private String details;

    public CalculationResponse() {}

    public CalculationResponse(String operation, Double result, String functionType) {
        this.operation = operation;
        this.result = result;
        this.functionType = functionType;
    }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public Double getResult() { return result; }
    public void setResult(Double result) { this.result = result; }

    public String getFunctionType() { return functionType; }
    public void setFunctionType(String functionType) { this.functionType = functionType; }

    public Long getComputationTimeMs() { return computationTimeMs; }
    public void setComputationTimeMs(Long computationTimeMs) { this.computationTimeMs = computationTimeMs; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}