package entity;

import javax.persistence.*;
import java.time.LocalDateTime;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import lombok.AllArgsConstructor;

// @Data
// @NoArgsConstructor
// @AllArgsConstructor
@Entity
@Table(name = "composite_functions")
public class CompositeFunctionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long compositeId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @Column(name = "composite_name", nullable = false, length = 100)
    private String compositeName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_function_id", nullable = false)
    private FunctionEntity firstFunction;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_function_id", nullable = false)
    private FunctionEntity secondFunction;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "x_from")
    private Double xFrom;

    @Column(name = "x_to")
    private Double xTo;

    @Column(name = "operation_method", length = 50)
    private String operationMethod;
    
    public CompositeFunctionEntity() {}
    
    public CompositeFunctionEntity(UserEntity user, String compositeName, 
                                  FunctionEntity firstFunction, FunctionEntity secondFunction) {
        this.user = user;
        this.compositeName = compositeName;
        this.firstFunction = firstFunction;
        this.secondFunction = secondFunction;
        this.createdAt = LocalDateTime.now();
    }
    
    public Long getCompositeId() { return compositeId; }
    public void setCompositeId(Long compositeId) { this.compositeId = compositeId; }
    
    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
    
    public String getCompositeName() { return compositeName; }
    public void setCompositeName(String compositeName) { this.compositeName = compositeName; }
    
    public FunctionEntity getFirstFunction() { return firstFunction; }
    public void setFirstFunction(FunctionEntity firstFunction) { this.firstFunction = firstFunction; }
    
    public FunctionEntity getSecondFunction() { return secondFunction; }
    public void setSecondFunction(FunctionEntity secondFunction) { this.secondFunction = secondFunction; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Double getXFrom() { return xFrom; }
    public void setXFrom(Double xFrom) { this.xFrom = xFrom; }

    public Double getXTo() { return xTo; }
    public void setXTo(Double xTo) { this.xTo = xTo; }

    public String getOperationMethod() { return operationMethod; }
    public void setOperationMethod(String operationMethod) { this.operationMethod = operationMethod; }
}