package manual.mapper;

import manual.entity.FunctionEntity;
import manual.entity.UserEntity;
import manual.dto.CreateFunctionRequest;
import manual.dto.FunctionResponse;
import java.util.logging.Logger;

public class FunctionMapper {
    private static final Logger logger = Logger.getLogger(FunctionMapper.class.getName());

    public static FunctionEntity toEntity(CreateFunctionRequest request, UserEntity user) {
        logger.fine("Mapping CreateFunctionRequest to FunctionEntity: " + request.getFunctionName());
        return new FunctionEntity(
                user,
                request.getFunctionName(),
                request.getFunctionType(),
                request.getFunctionExpression(),
                request.getXFrom(),
                request.getXTo()
        );
    }

    public static FunctionResponse toResponse(FunctionEntity entity) {
        logger.fine("Mapping FunctionEntity to FunctionResponse: " + entity.getFunctionId());
        return new FunctionResponse(
                entity.getFunctionId(),
                entity.getUser().getUserId(),
                entity.getFunctionName(),
                entity.getFunctionType(),
                entity.getFunctionExpression(),
                entity.getXFrom(),
                entity.getXTo(),
                entity.getCreatedAt()
        );
    }
}