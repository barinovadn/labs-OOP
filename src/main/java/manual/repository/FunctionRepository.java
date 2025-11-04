package manual.repository;

import manual.SqlFileReader;
import manual.dto.FunctionDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FunctionRepository {
    private static final Logger logger = Logger.getLogger(FunctionRepository.class.getName());
    private Connection connection;
    private final String CREATE_SQL;
    private final String READ_SQL;
    private final String UPDATE_SQL;
    private final String DELETE_SQL;
    private final String READ_BY_USER_SQL;

    public FunctionRepository(Connection connection) {
        this.connection = connection;
        this.CREATE_SQL = SqlFileReader.readSqlFile("FunctionCreate");
        this.READ_SQL = SqlFileReader.readSqlFile("FunctionRead");
        this.UPDATE_SQL = SqlFileReader.readSqlFile("FunctionUpdate");
        this.DELETE_SQL = SqlFileReader.readSqlFile("FunctionDelete");
        this.READ_BY_USER_SQL = SqlFileReader.readSqlFile("FunctionReadByUserId");
        logger.info("FunctionRepository initialized with SQL files");
    }

    public Long create(FunctionDto function) throws SQLException {
        logger.info("Creating function: " + function.getFunctionName());
        try (PreparedStatement stmt = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, function.getUserId());
            stmt.setString(2, function.getFunctionName());
            stmt.setString(3, function.getFunctionType());
            stmt.setString(4, function.getFunctionExpression());
            stmt.setDouble(5, function.getXFrom());
            stmt.setDouble(6, function.getXTo());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            Long functionId = keys.next() ? keys.getLong(1) : null;
            logger.info("Function created with ID: " + functionId);
            return functionId;
        } catch (SQLException e) {
            logger.severe("Failed to create function: " + e.getMessage());
            throw e;
        }
    }

    public FunctionDto findById(Long functionId) throws SQLException {
        logger.fine("Finding function by ID: " + functionId);
        try (PreparedStatement stmt = connection.prepareStatement(READ_SQL)) {
            stmt.setLong(1, functionId);
            ResultSet rs = stmt.executeQuery();
            FunctionDto result = rs.next() ? mapToDto(rs) : null;
            logger.fine("Function found: " + (result != null));
            return result;
        } catch (SQLException e) {
            logger.severe("Failed to find function by ID " + functionId + ": " + e.getMessage());
            throw e;
        }
    }

    public List<FunctionDto> findByUserId(Long userId) throws SQLException {
        logger.fine("Finding functions by user ID: " + userId);
        try (PreparedStatement stmt = connection.prepareStatement(READ_BY_USER_SQL)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            List<FunctionDto> functions = new ArrayList<>();
            while (rs.next()) {
                functions.add(mapToDto(rs));
            }
            logger.fine("Found " + functions.size() + " functions for user " + userId);
            return functions;
        } catch (SQLException e) {
            logger.severe("Failed to find functions for user ID " + userId + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean update(FunctionDto function) throws SQLException {
        logger.info("Updating function ID: " + function.getFunctionId());
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, function.getFunctionName());
            stmt.setString(2, function.getFunctionType());
            stmt.setString(3, function.getFunctionExpression());
            stmt.setDouble(4, function.getXFrom());
            stmt.setDouble(5, function.getXTo());
            stmt.setLong(6, function.getFunctionId());
            boolean result = stmt.executeUpdate() > 0;
            logger.info("Function update " + (result ? "successful" : "failed"));
            return result;
        } catch (SQLException e) {
            logger.severe("Failed to update function ID " + function.getFunctionId() + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean delete(Long functionId) throws SQLException {
        logger.info("Deleting function ID: " + functionId);
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, functionId);
            boolean result = stmt.executeUpdate() > 0;
            logger.info("Function deletion " + (result ? "successful" : "failed"));
            return result;
        } catch (SQLException e) {
            logger.severe("Failed to delete function ID " + functionId + ": " + e.getMessage());
            throw e;
        }
    }

    private FunctionDto mapToDto(ResultSet rs) throws SQLException {
        return new FunctionDto(
                rs.getLong("function_id"),
                rs.getLong("user_id"),
                rs.getString("function_name"),
                rs.getString("function_type"),
                rs.getString("function_expression"),
                rs.getDouble("x_from"),
                rs.getDouble("x_to"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}