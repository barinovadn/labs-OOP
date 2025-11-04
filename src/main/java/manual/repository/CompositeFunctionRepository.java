package manual.repository;

import manual.SqlFileReader;
import manual.dto.CompositeFunctionDto;
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

    public Long create(CompositeFunctionDto composite) throws SQLException {
        logger.info("Creating composite function: " + composite.getCompositeName());
        try (PreparedStatement stmt = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, composite.getUserId());
            stmt.setString(2, composite.getCompositeName());
            stmt.setLong(3, composite.getFirstFunctionId());
            stmt.setLong(4, composite.getSecondFunctionId());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            Long compositeId = keys.next() ? keys.getLong(1) : null;
            logger.info("Composite function created with ID: " + compositeId);
            return compositeId;
        } catch (SQLException e) {
            logger.severe("Failed to create composite function: " + e.getMessage());
            throw e;
        }
    }

    public CompositeFunctionDto findById(Long compositeId) throws SQLException {
        logger.fine("Finding composite function by ID: " + compositeId);
        try (PreparedStatement stmt = connection.prepareStatement(READ_SQL)) {
            stmt.setLong(1, compositeId);
            ResultSet rs = stmt.executeQuery();
            CompositeFunctionDto result = rs.next() ? mapToDto(rs) : null;
            logger.fine("Composite function found: " + (result != null));
            return result;
        } catch (SQLException e) {
            logger.severe("Failed to find composite function by ID " + compositeId + ": " + e.getMessage());
            throw e;
        }
    }

    public List<CompositeFunctionDto> findByUserId(Long userId) throws SQLException {
        logger.fine("Finding composite functions by user ID: " + userId);
        try (PreparedStatement stmt = connection.prepareStatement(READ_BY_USER_SQL)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            List<CompositeFunctionDto> composites = new ArrayList<>();
            while (rs.next()) {
                composites.add(mapToDto(rs));
            }
            logger.fine("Found " + composites.size() + " composite functions for user " + userId);
            return composites;
        } catch (SQLException e) {
            logger.severe("Failed to find composite functions for user ID " + userId + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean update(CompositeFunctionDto composite) throws SQLException {
        logger.info("Updating composite function ID: " + composite.getCompositeId());
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, composite.getCompositeName());
            stmt.setLong(2, composite.getFirstFunctionId());
            stmt.setLong(3, composite.getSecondFunctionId());
            stmt.setLong(4, composite.getCompositeId());
            boolean result = stmt.executeUpdate() > 0;
            logger.info("Composite function update " + (result ? "successful" : "failed"));
            return result;
        } catch (SQLException e) {
            logger.severe("Failed to update composite function ID " + composite.getCompositeId() + ": " + e.getMessage());
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

    private CompositeFunctionDto mapToDto(ResultSet rs) throws SQLException {
        return new CompositeFunctionDto(
                rs.getLong("composite_id"),
                rs.getLong("user_id"),
                rs.getString("composite_name"),
                rs.getLong("first_function_id"),
                rs.getLong("second_function_id"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}