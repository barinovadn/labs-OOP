package manual.mapper;

import manual.entity.CompositeFunctionEntity;
import manual.entity.UserEntity;
import manual.entity.FunctionEntity;
import manual.dto.CreateCompositeFunctionRequest;
import manual.dto.CompositeFunctionResponse;
import java.util.logging.Logger;

public class CompositeFunctionMapper {
    private static final Logger logger = Logger.getLogger(CompositeFunctionMapper.class.getName());

    public static CompositeFunctionEntity toEntity(CreateCompositeFunctionRequest request,
                                                   UserEntity user, FunctionEntity firstFunction,
                                                   FunctionEntity secondFunction) {
        logger.fine("Mapping CreateCompositeFunctionRequest to CompositeFunctionEntity: " + request.getCompositeName());
        return new CompositeFunctionEntity(
                user,
                request.getCompositeName(),
                firstFunction,
                secondFunction
        );
    }

    public static CompositeFunctionResponse toResponse(CompositeFunctionEntity entity) {
        logger.fine("Mapping CompositeFunctionEntity to CompositeFunctionResponse: " + entity.getCompositeId());
        return new CompositeFunctionResponse(
                entity.getCompositeId(),
                entity.getUser().getUserId(),
                entity.getCompositeName(),
                entity.getFirstFunction().getFunctionId(),
                entity.getSecondFunction().getFunctionId(),
                entity.getCreatedAt()
        );
    }
}