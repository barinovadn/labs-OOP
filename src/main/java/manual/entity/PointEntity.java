package manual.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "computed_points")
public class PointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long pointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function_id", nullable = false)
    private FunctionEntity function;

    @Column(name = "x_value", nullable = false, precision = 15, scale = 8)
    private Double xValue;

    @Column(name = "y_value", nullable = false, precision = 15, scale = 8)
    private Double yValue;

    @Column(name = "computed_at")
    private LocalDateTime computedAt;

    public PointEntity() {}

    public PointEntity(FunctionEntity function, Double xValue, Double yValue) {
        this.function = function;
        this.xValue = xValue;
        this.yValue = yValue;
        this.computedAt = LocalDateTime.now();
    }

    public Long getPointId() { return pointId; }
    public void setPointId(Long pointId) { this.pointId = pointId; }

    public FunctionEntity getFunction() { return function; }
    public void setFunction(FunctionEntity function) { this.function = function; }

    public Double getXValue() { return xValue; }
    public void setXValue(Double xValue) { this.xValue = xValue; }

    public Double getYValue() { return yValue; }
    public void setYValue(Double yValue) { this.yValue = yValue; }

    public LocalDateTime getComputedAt() { return computedAt; }
    public void setComputedAt(LocalDateTime computedAt) { this.computedAt = computedAt; }
}