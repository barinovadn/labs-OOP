package repository;

import entity.FunctionEntity;
import entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunctionRepository extends JpaRepository<FunctionEntity, Long> {

    List<FunctionEntity> findByUser(UserEntity user);

    List<FunctionEntity> findByFunctionType(String functionType);

    List<FunctionEntity> findByFunctionNameContainingIgnoreCase(String functionName);

    List<FunctionEntity> findByFunctionTypeContainingIgnoreCase(String functionType);

    List<FunctionEntity> findByFunctionExpressionContainingIgnoreCase(String expression);

    @Query("SELECT f FROM FunctionEntity f WHERE f.xFrom >= :minX AND f.xTo <= :maxX")
    List<FunctionEntity> findFunctionsInRange(@Param("minX") Double minX, @Param("maxX") Double maxX);

    @Query("SELECT f FROM FunctionEntity f WHERE SIZE(f.computedPoints) > :minPoints")
    List<FunctionEntity> findFunctionsWithMinimumPoints(@Param("minPoints") int minPoints);

    long countByUser(UserEntity user);
}