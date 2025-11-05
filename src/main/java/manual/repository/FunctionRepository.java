package manual.repository;

import manual.SqlFileReader;
import manual.entity.FunctionEntity;
import manual.entity.UserEntity;
import manual.dto.CreateFunctionRequest;
import manual.dto.FunctionResponse;
import manual.mapper.FunctionMapper;
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
    private final String READ_ALL_SQL;

    public FunctionRepository(Connection connection) {
        this.connection = connection;
        this.CREATE_SQL = SqlFileReader.readSqlFile("FunctionCreate");
        this.READ_SQL = SqlFileReader.readSqlFile("FunctionRead");
        this.UPDATE_SQL = SqlFileReader.readSqlFile("FunctionUpdate");
        this.DELETE_SQL = SqlFileReader.readSqlFile("FunctionDelete");
        this.READ_BY_USER_SQL = SqlFileReader.readSqlFile("FunctionReadByUserId");
        this.READ_ALL_SQL = "SELECT * FROM functions";
        logger.info("FunctionRepository initialized with SQL files");
    }

    public FunctionResponse create(CreateFunctionRequest request, UserEntity user) throws SQLException {
        logger.info("Creating function: " + request.getFunctionName());
        try (PreparedStatement stmt = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, user.getUserId());
            stmt.setString(2, request.getFunctionName());
            stmt.setString(3, request.getFunctionType());
            stmt.setString(4, request.getFunctionExpression());
            stmt.setDouble(5, request.getXFrom());
            stmt.setDouble(6, request.getXTo());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                Long functionId = keys.getLong(1);
                logger.info("Function created with ID: " + functionId);

                FunctionEntity entity = findEntityById(functionId);
                return FunctionMapper.toResponse(entity);
            }
            throw new SQLException("Failed to get generated function ID");
        } catch (SQLException e) {
            logger.severe("Failed to create function: " + e.getMessage());
            throw e;
        }
    }

    public FunctionResponse findById(Long functionId) throws SQLException {
        logger.fine("Finding function by ID: " + functionId);
        FunctionEntity entity = findEntityById(functionId);
        return entity != null ? FunctionMapper.toResponse(entity) : null;
    }

    private FunctionEntity findEntityById(Long functionId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(READ_SQL)) {
            stmt.setLong(1, functionId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? mapToEntity(rs) : null;
        }
    }

    public List<FunctionResponse> findByUserId(Long userId) throws SQLException {
        logger.fine("Finding functions by user ID: " + userId);
        try (PreparedStatement stmt = connection.prepareStatement(READ_BY_USER_SQL)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            List<FunctionResponse> functions = new ArrayList<>();
            while (rs.next()) {
                FunctionEntity entity = mapToEntity(rs);
                functions.add(FunctionMapper.toResponse(entity));
            }
            logger.fine("Found " + functions.size() + " functions for user " + userId);
            return functions;
        } catch (SQLException e) {
            logger.severe("Failed to find functions for user ID " + userId + ": " + e.getMessage());
            throw e;
        }
    }

    public List<FunctionResponse> findAll() throws SQLException {
        logger.fine("Finding all functions");
        try (PreparedStatement stmt = connection.prepareStatement(READ_ALL_SQL)) {
            ResultSet rs = stmt.executeQuery();
            List<FunctionResponse> functions = new ArrayList<>();
            while (rs.next()) {
                FunctionEntity entity = mapToEntity(rs);
                functions.add(FunctionMapper.toResponse(entity));
            }
            logger.fine("Found " + functions.size() + " functions");
            return functions;
        } catch (SQLException e) {
            logger.severe("Failed to find all functions: " + e.getMessage());
            throw e;
        }
    }

    public FunctionResponse update(Long functionId, CreateFunctionRequest request) throws SQLException {
        logger.info("Updating function ID: " + functionId);
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, request.getFunctionName());
            stmt.setString(2, request.getFunctionType());
            stmt.setString(3, request.getFunctionExpression());
            stmt.setDouble(4, request.getXFrom());
            stmt.setDouble(5, request.getXTo());
            stmt.setLong(6, functionId);

            boolean result = stmt.executeUpdate() > 0;
            logger.info("Function update " + (result ? "successful" : "failed"));

            if (result) {
                FunctionEntity entity = findEntityById(functionId);
                return FunctionMapper.toResponse(entity);
            }
            return null;
        } catch (SQLException e) {
            logger.severe("Failed to update function ID " + functionId + ": " + e.getMessage());
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

    private FunctionEntity mapToEntity(ResultSet rs) throws SQLException {
        FunctionEntity entity = new FunctionEntity();
        entity.setFunctionId(rs.getLong("function_id"));
        entity.setFunctionName(rs.getString("function_name"));
        entity.setFunctionType(rs.getString("function_type"));
        entity.setFunctionExpression(rs.getString("function_expression"));
        entity.setXFrom(rs.getDouble("x_from"));
        entity.setXTo(rs.getDouble("x_to"));
        entity.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        UserEntity user = new UserEntity();
        user.setUserId(rs.getLong("user_id"));
        entity.setUser(user);

        return entity;
    }
}