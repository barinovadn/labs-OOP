package entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import lombok.AllArgsConstructor;

// @Data
// @NoArgsConstructor
// @AllArgsConstructor
@Entity
@Table(name = "functions")
public class FunctionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long functionId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @Column(name = "function_name", nullable = false, length = 100)
    private String functionName;
    
    @Column(name = "function_type", nullable = false, length = 50)
    private String functionType;
    
    @Column(name = "function_expression", columnDefinition = "TEXT")
    private String functionExpression;
    
    @Column(name = "x_from")
    private Double xFrom;
    
    @Column(name = "x_to")
    private Double xTo;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "function", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PointEntity> computedPoints = new ArrayList<>();
    
    public FunctionEntity() {}
    
    public FunctionEntity(UserEntity user, String functionName, String functionType, 
                         String functionExpression, Double xFrom, Double xTo) {
        this.user = user;
        this.functionName = functionName;
        this.functionType = functionType;
        this.functionExpression = functionExpression;
        this.xFrom = xFrom;
        this.xTo = xTo;
        this.createdAt = LocalDateTime.now();
    }
    
    public Long getFunctionId() { return functionId; }
    public void setFunctionId(Long functionId) { this.functionId = functionId; }
    
    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
    
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
    
    public List<PointEntity> getComputedPoints() { return computedPoints; }
    public void setComputedPoints(List<PointEntity> computedPoints) { this.computedPoints = computedPoints; }
}