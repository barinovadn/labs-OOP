package manual.repository;

import manual.SqlFileReader;
import manual.dto.PointDto;
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

    public PointRepository(Connection connection) {
        this.connection = connection;
        this.CREATE_SQL = SqlFileReader.readSqlFile("PointsCreate");
        this.READ_SQL = SqlFileReader.readSqlFile("PointsRead");
        this.UPDATE_SQL = SqlFileReader.readSqlFile("PointsUpdate");
        this.DELETE_SQL = SqlFileReader.readSqlFile("PointsDelete");
        this.READ_BY_FUNCTION_SQL = SqlFileReader.readSqlFile("PointsReadByFunction");
        logger.info("PointRepository initialized with SQL files");
    }

    public Long create(PointDto point) throws SQLException {
        logger.fine("Creating point for function ID: " + point.getFunctionId());
        try (PreparedStatement stmt = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, point.getFunctionId());
            stmt.setDouble(2, point.getXValue());
            stmt.setDouble(3, point.getYValue());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            Long pointId = keys.next() ? keys.getLong(1) : null;
            logger.fine("Point created with ID: " + pointId);
            return pointId;
        } catch (SQLException e) {
            logger.severe("Failed to create point: " + e.getMessage());
            throw e;
        }
    }

    public PointDto findById(Long pointId) throws SQLException {
        logger.finest("Finding point by ID: " + pointId);
        try (PreparedStatement stmt = connection.prepareStatement(READ_SQL)) {
            stmt.setLong(1, pointId);
            ResultSet rs = stmt.executeQuery();
            PointDto result = rs.next() ? mapToDto(rs) : null;
            return result;
        } catch (SQLException e) {
            logger.severe("Failed to find point by ID " + pointId + ": " + e.getMessage());
            throw e;
        }
    }

    public List<PointDto> findByFunctionId(Long functionId) throws SQLException {
        logger.fine("Finding points by function ID: " + functionId);
        try (PreparedStatement stmt = connection.prepareStatement(READ_BY_FUNCTION_SQL)) {
            stmt.setLong(1, functionId);
            ResultSet rs = stmt.executeQuery();
            List<PointDto> points = new ArrayList<>();
            while (rs.next()) {
                points.add(mapToDto(rs));
            }
            logger.fine("Found " + points.size() + " points for function " + functionId);
            return points;
        } catch (SQLException e) {
            logger.severe("Failed to find points for function ID " + functionId + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean update(PointDto point) throws SQLException {
        logger.fine("Updating point ID: " + point.getPointId());
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            stmt.setDouble(1, point.getXValue());
            stmt.setDouble(2, point.getYValue());
            stmt.setLong(3, point.getPointId());
            boolean result = stmt.executeUpdate() > 0;
            logger.fine("Point update " + (result ? "successful" : "failed"));
            return result;
        } catch (SQLException e) {
            logger.severe("Failed to update point ID " + point.getPointId() + ": " + e.getMessage());
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

    private PointDto mapToDto(ResultSet rs) throws SQLException {
        return new PointDto(
                rs.getLong("point_id"),
                rs.getLong("function_id"),
                rs.getDouble("x_value"),
                rs.getDouble("y_value"),
                rs.getTimestamp("computed_at").toLocalDateTime()
        );
    }
}