package manual.dto;

public class CalculationRequest {
    private String operation;
    private TabulatedFunctionRequest function;
    private TabulatedFunctionRequest functionA;
    private TabulatedFunctionRequest functionB;
    private Integer threads;
    private Double step;
    private String differentialType; // LEFT/ RIGHT/ MIDDLE

    public CalculationRequest() {}

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public TabulatedFunctionRequest getFunction() { return function; }
    public void setFunction(TabulatedFunctionRequest function) { this.function = function; }

    public TabulatedFunctionRequest getFunctionA() { return functionA; }
    public void setFunctionA(TabulatedFunctionRequest functionA) { this.functionA = functionA; }

    public TabulatedFunctionRequest getFunctionB() { return functionB; }
    public void setFunctionB(TabulatedFunctionRequest functionB) { this.functionB = functionB; }

    public Integer getThreads() { return threads; }
    public void setThreads(Integer threads) { this.threads = threads; }

    public Double getStep() { return step; }
    public void setStep(Double step) { this.step = step; }

    public String getDifferentialType() { return differentialType; }
    public void setDifferentialType(String differentialType) { this.differentialType = differentialType; }
}