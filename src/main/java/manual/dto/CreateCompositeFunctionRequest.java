package manual.dto;

public class CreateCompositeFunctionRequest {
    private Long userId;
    private String compositeName;
    private Long firstFunctionId;
    private Long secondFunctionId;

    public CreateCompositeFunctionRequest() {}

    public CreateCompositeFunctionRequest(Long userId, String compositeName,
                                          Long firstFunctionId, Long secondFunctionId) {
        this.userId = userId;
        this.compositeName = compositeName;
        this.firstFunctionId = firstFunctionId;
        this.secondFunctionId = secondFunctionId;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCompositeName() { return compositeName; }
    public void setCompositeName(String compositeName) { this.compositeName = compositeName; }

    public Long getFirstFunctionId() { return firstFunctionId; }
    public void setFirstFunctionId(Long firstFunctionId) { this.firstFunctionId = firstFunctionId; }

    public Long getSecondFunctionId() { return secondFunctionId; }
    public void setSecondFunctionId(Long secondFunctionId) { this.secondFunctionId = secondFunctionId; }
}