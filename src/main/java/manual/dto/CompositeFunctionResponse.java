package manual.dto;

import java.time.LocalDateTime;

public class CompositeFunctionResponse {
    private Long compositeId;
    private Long userId;
    private String compositeName;
    private Long firstFunctionId;
    private Long secondFunctionId;
    private LocalDateTime createdAt;

    public CompositeFunctionResponse() {}

    public CompositeFunctionResponse(Long compositeId, Long userId, String compositeName,
                                     Long firstFunctionId, Long secondFunctionId, LocalDateTime createdAt) {
        this.compositeId = compositeId;
        this.userId = userId;
        this.compositeName = compositeName;
        this.firstFunctionId = firstFunctionId;
        this.secondFunctionId = secondFunctionId;
        this.createdAt = createdAt;
    }

    public Long getCompositeId() { return compositeId; }
    public void setCompositeId(Long compositeId) { this.compositeId = compositeId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCompositeName() { return compositeName; }
    public void setCompositeName(String compositeName) { this.compositeName = compositeName; }

    public Long getFirstFunctionId() { return firstFunctionId; }
    public void setFirstFunctionId(Long firstFunctionId) { this.firstFunctionId = firstFunctionId; }

    public Long getSecondFunctionId() { return secondFunctionId; }
    public void setSecondFunctionId(Long secondFunctionId) { this.secondFunctionId = secondFunctionId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}