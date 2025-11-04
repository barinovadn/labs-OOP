package repository;

import entity.CompositeFunctionEntity;
import entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompositeFunctionRepository extends JpaRepository<CompositeFunctionEntity, Long> {

    List<CompositeFunctionEntity> findByUser(UserEntity user);

    List<CompositeFunctionEntity> findByCompositeNameContainingIgnoreCase(String compositeName);

    @Query("SELECT cf FROM CompositeFunctionEntity cf WHERE cf.firstFunction.functionId = :functionId OR cf.secondFunction.functionId = :functionId")
    List<CompositeFunctionEntity> findCompositeFunctionsUsingFunction(@Param("functionId") Long functionId);

    @Query("SELECT cf FROM CompositeFunctionEntity cf WHERE cf.user.userId = :userId AND SIZE(cf.user.compositeFunctions) >= :minComposites")
    List<CompositeFunctionEntity> findUsersWithMinimumComposites(@Param("userId") Long userId,
                                                                 @Param("minComposites") int minComposites);
}