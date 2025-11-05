package manual.repository;

import manual.SqlFileReader;
import manual.entity.CompositeFunctionEntity;
import manual.entity.UserEntity;
import manual.entity.FunctionEntity;
import manual.dto.CreateCompositeFunctionRequest;
import manual.dto.CompositeFunctionResponse;
import manual.mapper.CompositeFunctionMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CompositeFunctionRepository {
    private static final Logger logger = Logger.getLogger(CompositeFunctionRepository.class.getName());
    private Connection connection;
    private final String CREATE_SQL;
    private final String READ_SQL;
    private final String UPDATE_SQL;
    private final String DELETE_SQL;
    private final String READ_BY_USER_SQL;

    public CompositeFunctionRepository(Connection connection) {
        this.connection = connection;
        this.CREATE_SQL = SqlFileReader.readSqlFile("CompositeFunctionCreate");
        this.READ_SQL = SqlFileReader.readSqlFile("CompositeFunctionRead");
        this.UPDATE_SQL = SqlFileReader.readSqlFile("CompositeFunctionUpdate");
        this.DELETE_SQL = SqlFileReader.readSqlFile("CompositeFunctionDelete");
        this.READ_BY_USER_SQL = SqlFileReader.readSqlFile("CompositeFunctionReadByUserId");
        logger.info("CompositeFunctionRepository initialized with SQL files");
    }

    public CompositeFunctionResponse create(CreateCompositeFunctionRequest request, UserEntity user,
                                            FunctionEntity firstFunction, FunctionEntity secondFunction) throws SQLException {
        logger.info("Creating composite function: " + request.getCompositeName());
        try (PreparedStatement stmt = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, user.getUserId());
            stmt.setString(2, request.getCompositeName());
            stmt.setLong(3, firstFunction.getFunctionId());
            stmt.setLong(4, secondFunction.getFunctionId());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                Long compositeId = keys.getLong(1);
                logger.info("Composite function created with ID: " + compositeId);

                CompositeFunctionEntity entity = findEntityById(compositeId);
                return CompositeFunctionMapper.toResponse(entity);
            }
            throw new SQLException("Failed to get generated composite function ID");
        } catch (SQLException e) {
            logger.severe("Failed to create composite function: " + e.getMessage());
            throw e;
        }
    }

    public CompositeFunctionResponse findById(Long compositeId) throws SQLException {
        logger.fine("Finding composite function by ID: " + compositeId);
        CompositeFunctionEntity entity = findEntityById(compositeId);
        return entity != null ? CompositeFunctionMapper.toResponse(entity) : null;
    }

    private CompositeFunctionEntity findEntityById(Long compositeId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(READ_SQL)) {
            stmt.setLong(1, compositeId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? mapToEntity(rs) : null;
        }
    }

    public List<CompositeFunctionResponse> findByUserId(Long userId) throws SQLException {
        logger.fine("Finding composite functions by user ID: " + userId);
        try (PreparedStatement stmt = connection.prepareStatement(READ_BY_USER_SQL)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            List<CompositeFunctionResponse> composites = new ArrayList<>();
            while (rs.next()) {
                CompositeFunctionEntity entity = mapToEntity(rs);
                composites.add(CompositeFunctionMapper.toResponse(entity));
            }
            logger.fine("Found " + composites.size() + " composite functions for user " + userId);
            return composites;
        } catch (SQLException e) {
            logger.severe("Failed to find composite functions for user ID " + userId + ": " + e.getMessage());
            throw e;
        }
    }

    public CompositeFunctionResponse update(Long compositeId, CreateCompositeFunctionRequest request) throws SQLException {
        logger.info("Updating composite function ID: " + compositeId);
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, request.getCompositeName());
            stmt.setLong(2, request.getFirstFunctionId());
            stmt.setLong(3, request.getSecondFunctionId());
            stmt.setLong(4, compositeId);
            boolean result = stmt.executeUpdate() > 0;
            logger.info("Composite function update " + (result ? "successful" : "failed"));

            if (result) {
                CompositeFunctionEntity entity = findEntityById(compositeId);
                return CompositeFunctionMapper.toResponse(entity);
            }
            return null;
        } catch (SQLException e) {
            logger.severe("Failed to update composite function ID " + compositeId + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean delete(Long compositeId) throws SQLException {
        logger.info("Deleting composite function ID: " + compositeId);
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, compositeId);
            boolean result = stmt.executeUpdate() > 0;
            logger.info("Composite function deletion " + (result ? "successful" : "failed"));
            return result;
        } catch (SQLException e) {
            logger.severe("Failed to delete composite function ID " + compositeId + ": " + e.getMessage());
            throw e;
        }
    }

    private CompositeFunctionEntity mapToEntity(ResultSet rs) throws SQLException {
        CompositeFunctionEntity entity = new CompositeFunctionEntity();
        entity.setCompositeId(rs.getLong("composite_id"));
        entity.setCompositeName(rs.getString("composite_name"));
        entity.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        UserEntity user = new UserEntity();
        user.setUserId(rs.getLong("user_id"));
        entity.setUser(user);

        FunctionEntity firstFunction = new FunctionEntity();
        firstFunction.setFunctionId(rs.getLong("first_function_id"));
        entity.setFirstFunction(firstFunction);

        FunctionEntity secondFunction = new FunctionEntity();
        secondFunction.setFunctionId(rs.getLong("second_function_id"));
        entity.setSecondFunction(secondFunction);

        return entity;
    }
}