package manual.repository;

import manual.SqlFileReader;
import manual.entity.UserEntity;
import manual.dto.CreateUserRequest;
import manual.dto.UserResponse;
import manual.mapper.UserMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserRepository {
    private static final Logger logger = Logger.getLogger(UserRepository.class.getName());
    private Connection connection;
    private final String CREATE_SQL;
    private final String READ_SQL;
    private final String UPDATE_SQL;
    private final String DELETE_SQL;
    private final String READ_ALL_SQL;
    private final String READ_BY_USERNAME_SQL;

    public UserRepository(Connection connection) {
        this.connection = connection;
        this.CREATE_SQL = SqlFileReader.readSqlFile("UserCreate");
        this.READ_SQL = SqlFileReader.readSqlFile("UserRead");
        this.UPDATE_SQL = SqlFileReader.readSqlFile("UserUpdate");
        this.DELETE_SQL = SqlFileReader.readSqlFile("UserDelete");
        this.READ_ALL_SQL = SqlFileReader.readSqlFile("UserReadAll");
        this.READ_BY_USERNAME_SQL = SqlFileReader.readSqlFile("UserReadByUsername");
        logger.info("UserRepository initialized with SQL files");
    }

    public UserResponse create(CreateUserRequest request) throws SQLException {
        logger.info("Creating user: " + request.getUsername());
        try (PreparedStatement stmt = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, request.getUsername());
            stmt.setString(2, request.getPassword());
            stmt.setString(3, request.getEmail());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                Long userId = keys.getLong(1);
                logger.info("User created with ID: " + userId);

                UserEntity entity = findEntityById(userId);
                return UserMapper.toResponse(entity);
            }
            throw new SQLException("Failed to get generated user ID");
        } catch (SQLException e) {
            logger.severe("Failed to create user: " + e.getMessage());
            throw e;
        }
    }

    public UserResponse findById(Long userId) throws SQLException {
        logger.fine("Finding user by ID: " + userId);
        UserEntity entity = findEntityById(userId);
        return entity != null ? UserMapper.toResponse(entity) : null;
    }

    public UserEntity findEntityByUsername(String username) throws SQLException {
        logger.fine("Finding user by username: " + username);
        try (PreparedStatement stmt = connection.prepareStatement(READ_BY_USERNAME_SQL)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? mapToEntity(rs) : null;
        }
    }

    private UserEntity findEntityById(Long userId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(READ_SQL)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? mapToEntity(rs) : null;
        }
    }

    public List<UserResponse> findAll() throws SQLException {
        logger.fine("Finding all users");
        try (PreparedStatement stmt = connection.prepareStatement(READ_ALL_SQL)) {
            ResultSet rs = stmt.executeQuery();
            List<UserResponse> users = new ArrayList<>();
            while (rs.next()) {
                UserEntity entity = mapToEntity(rs);
                users.add(UserMapper.toResponse(entity));
            }
            logger.fine("Found " + users.size() + " users");
            return users;
        } catch (SQLException e) {
            logger.severe("Failed to find all users: " + e.getMessage());
            throw e;
        }
    }

    public UserResponse update(Long userId, CreateUserRequest request) throws SQLException {
        logger.info("Updating user ID: " + userId);
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, request.getUsername());
            stmt.setString(2, request.getPassword());
            stmt.setString(3, request.getEmail());
            stmt.setLong(4, userId);

            boolean result = stmt.executeUpdate() > 0;
            logger.info("User update " + (result ? "successful" : "failed"));

            if (result) {
                UserEntity entity = findEntityById(userId);
                return UserMapper.toResponse(entity);
            }
            return null;
        } catch (SQLException e) {
            logger.severe("Failed to update user ID " + userId + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean delete(Long userId) throws SQLException {
        logger.info("Deleting user ID: " + userId);
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, userId);
            boolean result = stmt.executeUpdate() > 0;
            logger.info("User deletion " + (result ? "successful" : "failed"));
            return result;
        } catch (SQLException e) {
            logger.severe("Failed to delete user ID " + userId + ": " + e.getMessage());
            throw e;
        }
    }

    private UserEntity mapToEntity(ResultSet rs) throws SQLException {
        UserEntity entity = new UserEntity();
        entity.setUserId(rs.getLong("user_id"));
        entity.setUsername(rs.getString("username"));
        entity.setPassword(rs.getString("password"));
        entity.setEmail(rs.getString("email"));
        entity.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return entity;
    }
}