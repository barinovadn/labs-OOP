package repository;

import entity.FunctionEntity;
import entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<PointEntity, Long> {

    List<PointEntity> findByFunction(FunctionEntity function);

    @Query("SELECT p FROM PointEntity p WHERE p.function.functionId = :functionId ORDER BY p.xValue")
    List<PointEntity> findByFunctionIdOrderByXValue(@Param("functionId") Long functionId);

    @Query("SELECT p FROM PointEntity p WHERE p.function.functionId = :functionId AND p.xValue BETWEEN :minX AND :maxX")
    List<PointEntity> findPointsInRange(@Param("functionId") Long functionId,
                                        @Param("minX") Double minX,
                                        @Param("maxX") Double maxX);

    void deleteByFunction(FunctionEntity function);

    long countByFunction(FunctionEntity function);
}