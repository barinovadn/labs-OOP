package service;

import dto.PointRequest;
import dto.PointResponse;
import entity.FunctionEntity;
import entity.PointEntity;
import repository.FunctionRepository;
import repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
@Transactional
public class PointService {
    private static final Logger logger = Logger.getLogger(PointService.class.getName());

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private FunctionRepository functionRepository;

    public PointResponse createPoint(Long functionId, PointRequest request) {
        logger.info("Creating point for function ID: " + functionId);
        FunctionEntity function = functionRepository.findById(functionId)
                .orElseThrow(() -> new RuntimeException("Function not found with id: " + functionId));

        PointEntity point = new PointEntity(function, request.getXValue(), request.getYValue());
        point = pointRepository.save(point);
        logger.info("Point created with ID: " + point.getPointId());
        return toResponse(point);
    }

    public PointResponse getPointById(Long id) {
        logger.info("Getting point by ID: " + id);
        PointEntity point = pointRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Point not found with id: " + id));
        return toResponse(point);
    }

    public List<PointResponse> getPointsByFunctionId(Long functionId) {
        logger.info("Getting points for function ID: " + functionId);
        FunctionEntity function = functionRepository.findById(functionId)
                .orElseThrow(() -> new RuntimeException("Function not found with id: " + functionId));
        return pointRepository.findByFunction(function).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PointResponse updatePoint(Long id, PointRequest request) {
        logger.info("Updating point with ID: " + id);
        PointEntity point = pointRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Point not found with id: " + id));

        point.setXValue(request.getXValue());
        point.setYValue(request.getYValue());

        point = pointRepository.save(point);
        logger.info("Point updated: " + id);
        return toResponse(point);
    }

    public void deletePoint(Long id) {
        logger.info("Deleting point with ID: " + id);
        if (!pointRepository.existsById(id)) {
            throw new RuntimeException("Point not found with id: " + id);
        }
        pointRepository.deleteById(id);
        logger.info("Point deleted: " + id);
    }

    private PointResponse toResponse(PointEntity point) {
        return new PointResponse(
                point.getPointId(),
                point.getFunction().getFunctionId(),
                point.getXValue(),
                point.getYValue(),
                point.getComputedAt()
        );
    }
}

