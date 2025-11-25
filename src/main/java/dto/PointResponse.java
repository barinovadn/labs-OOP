package dto;

import java.time.LocalDateTime;

public class PointResponse {
    private Long pointId;
    private Long functionId;
    private Double xValue;
    private Double yValue;
    private LocalDateTime computedAt;

    public PointResponse() {}

    public PointResponse(Long pointId, Long functionId, Double xValue, Double yValue, LocalDateTime computedAt) {
        this.pointId = pointId;
        this.functionId = functionId;
        this.xValue = xValue;
        this.yValue = yValue;
        this.computedAt = computedAt;
    }

    public Long getPointId() { return pointId; }
    public void setPointId(Long pointId) { this.pointId = pointId; }

    public Long getFunctionId() { return functionId; }
    public void setFunctionId(Long functionId) { this.functionId = functionId; }

    public Double getXValue() { return xValue; }
    public void setXValue(Double xValue) { this.xValue = xValue; }

    public Double getYValue() { return yValue; }
    public void setYValue(Double yValue) { this.yValue = yValue; }

    public LocalDateTime getComputedAt() { return computedAt; }
    public void setComputedAt(LocalDateTime computedAt) { this.computedAt = computedAt; }
}

