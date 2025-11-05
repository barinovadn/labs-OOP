package manual.mapper;

import manual.entity.PointEntity;
import manual.entity.FunctionEntity;
import manual.dto.CreatePointRequest;
import manual.dto.PointResponse;
import java.util.logging.Logger;

public class PointMapper {
    private static final Logger logger = Logger.getLogger(PointMapper.class.getName());

    public static PointEntity toEntity(CreatePointRequest request, FunctionEntity function) {
        logger.fine("Mapping CreatePointRequest to PointEntity for function: " + function.getFunctionId());
        return new PointEntity(
                function,
                request.getXValue(),
                request.getYValue()
        );
    }

    public static PointResponse toResponse(PointEntity entity) {
        logger.fine("Mapping PointEntity to PointResponse: " + entity.getPointId());
        return new PointResponse(
                entity.getPointId(),
                entity.getFunction().getFunctionId(),
                entity.getXValue(),
                entity.getYValue(),
                entity.getComputedAt()
        );
    }
}