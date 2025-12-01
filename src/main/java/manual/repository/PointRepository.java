package manual.repository;

import manual.SqlFileReader;
import manual.entity.PointEntity;
import manual.entity.FunctionEntity;
import manual.dto.CreatePointRequest;
import manual.dto.PointResponse;
import manual.mapper.PointMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PointRepository {
    private static final Logger logger = Logger.getLogger(PointRepository.class.getName());
    private Connection connection;
    private final String CREATE_SQL;
    private final String READ_SQL;
    private final String UPDATE_SQL;
    private final String DELETE_SQL;
    private final String READ_BY_FUNCTION_SQL;
    private final String READ_BY_FUNCTION_AND_X_SQL;

    public PointRepository(Connection connection) {
        this.connection = connection;
        this.CREATE_SQL = SqlFileReader.readSqlFile("PointsCreate");
        this.READ_SQL = SqlFileReader.readSqlFile("PointsRead");
        this.UPDATE_SQL = SqlFileReader.readSqlFile("PointsUpdate");
        this.DELETE_SQL = SqlFileReader.readSqlFile("PointsDelete");
        this.READ_BY_FUNCTION_SQL = SqlFileReader.readSqlFile("PointsReadByFunction");
        this.READ_BY_FUNCTION_AND_X_SQL = SqlFileReader.readSqlFile("PointsReadByFunctionAndX");
        logger.info("PointRepository initialized with SQL files");
    }

    public PointResponse create(CreatePointRequest request, FunctionEntity function) throws SQLException {
        logger.fine("Creating point for function ID: " + function.getFunctionId());
        try (PreparedStatement stmt = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, function.getFunctionId());
            stmt.setDouble(2, request.getXValue());
            stmt.setDouble(3, request.getYValue());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                Long pointId = keys.getLong(1);
                logger.fine("Point created with ID: " + pointId);

                PointEntity entity = findEntityById(pointId);
                return PointMapper.toResponse(entity);
            }
            throw new SQLException("Failed to get generated point ID");
        } catch (SQLException e) {
            logger.severe("Failed to create point: " + e.getMessage());
            throw e;
        }
    }
    
    public PointResponse create(CreatePointRequest request) throws SQLException {
        Long functionId = request.getFunctionId();
        if (functionId == null) {
            throw new SQLException("Function ID is required");
        }
        FunctionEntity function = new FunctionEntity();
        function.setFunctionId(functionId);
        return create(request, function);
    }

    public PointResponse findById(Long pointId) throws SQLException {
        logger.finest("Finding point by ID: " + pointId);
        PointEntity entity = findEntityById(pointId);
        return entity != null ? PointMapper.toResponse(entity) : null;
    }

    private PointEntity findEntityById(Long pointId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(READ_SQL)) {
            stmt.setLong(1, pointId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? mapToEntity(rs) : null;
        }
    }

    public List<PointResponse> findByFunctionId(Long functionId) throws SQLException {
        logger.fine("Finding points by function ID: " + functionId);
        try (PreparedStatement stmt = connection.prepareStatement(READ_BY_FUNCTION_SQL)) {
            stmt.setLong(1, functionId);
            ResultSet rs = stmt.executeQuery();
            List<PointResponse> points = new ArrayList<>();
            while (rs.next()) {
                PointEntity entity = mapToEntity(rs);
                points.add(PointMapper.toResponse(entity));
            }
            logger.fine("Found " + points.size() + " points for function " + functionId);
            return points;
        } catch (SQLException e) {
            logger.severe("Failed to find points for function ID " + functionId + ": " + e.getMessage());
            throw e;
        }
    }

    public PointResponse findByFunctionIdAndX(Long functionId, Double x) throws SQLException {
        logger.fine("Finding point by function ID: " + functionId + " and x: " + x);
        try (PreparedStatement stmt = connection.prepareStatement(READ_BY_FUNCTION_AND_X_SQL)) {
            stmt.setLong(1, functionId);
            stmt.setDouble(2, x);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                PointEntity entity = mapToEntity(rs);
                logger.fine("Point found in cache");
                return PointMapper.toResponse(entity);
            }
            logger.fine("Point not found in cache");
            return null;
        } catch (SQLException e) {
            logger.severe("Failed to find point by function ID " + functionId + " and x " + x + ": " + e.getMessage());
            throw e;
        }
    }

    public PointResponse update(Long pointId, CreatePointRequest request) throws SQLException {
        logger.fine("Updating point ID: " + pointId);
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            stmt.setDouble(1, request.getXValue());
            stmt.setDouble(2, request.getYValue());
            stmt.setLong(3, pointId);
            boolean result = stmt.executeUpdate() > 0;
            logger.fine("Point update " + (result ? "successful" : "failed"));

            if (result) {
                PointEntity entity = findEntityById(pointId);
                return PointMapper.toResponse(entity);
            }
            return null;
        } catch (SQLException e) {
            logger.severe("Failed to update point ID " + pointId + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean delete(Long pointId) throws SQLException {
        logger.fine("Deleting point ID: " + pointId);
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, pointId);
            boolean result = stmt.executeUpdate() > 0;
            logger.fine("Point deletion " + (result ? "successful" : "failed"));
            return result;
        } catch (SQLException e) {
            logger.severe("Failed to delete point ID " + pointId + ": " + e.getMessage());
            throw e;
        }
    }

    private PointEntity mapToEntity(ResultSet rs) throws SQLException {
        PointEntity entity = new PointEntity();
        entity.setPointId(rs.getLong("point_id"));
        entity.setXValue(rs.getDouble("x_value"));
        entity.setYValue(rs.getDouble("y_value"));
        entity.setComputedAt(rs.getTimestamp("computed_at").toLocalDateTime());

        FunctionEntity function = new FunctionEntity();
        function.setFunctionId(rs.getLong("function_id"));
        entity.setFunction(function);

        return entity;
    }
}