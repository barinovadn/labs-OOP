package manual.repository;

import manual.SqlFileReader;
import manual.dto.UserDto;
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

    public UserRepository(Connection connection) {
        this.connection = connection;
        this.CREATE_SQL = SqlFileReader.readSqlFile("UserCreate");
        this.READ_SQL = SqlFileReader.readSqlFile("UserRead");
        this.UPDATE_SQL = SqlFileReader.readSqlFile("UserUpdate");
        this.DELETE_SQL = SqlFileReader.readSqlFile("UserDelete");
        this.READ_ALL_SQL = SqlFileReader.readSqlFile("UserReadAll");
        logger.info("UserRepository initialized with SQL files");
    }

    public Long create(UserDto user) throws SQLException {
        logger.info("Creating user: " + user.getUsername());
        try (PreparedStatement stmt = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            Long userId = keys.next() ? keys.getLong(1) : null;
            logger.info("User created with ID: " + userId);
            return userId;
        } catch (SQLException e) {
            logger.severe("Failed to create user: " + e.getMessage());
            throw e;
        }
    }

    public UserDto findById(Long userId) throws SQLException {
        logger.fine("Finding user by ID: " + userId);
        try (PreparedStatement stmt = connection.prepareStatement(READ_SQL)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            UserDto result = rs.next() ? mapToDto(rs) : null;
            logger.fine("User found: " + (result != null));
            return result;
        } catch (SQLException e) {
            logger.severe("Failed to find user by ID " + userId + ": " + e.getMessage());
            throw e;
        }
    }

    public List<UserDto> findAll() throws SQLException {
        logger.fine("Finding all users");
        try (PreparedStatement stmt = connection.prepareStatement(READ_ALL_SQL)) {
            ResultSet rs = stmt.executeQuery();
            List<UserDto> users = new ArrayList<>();
            while (rs.next()) {
                users.add(mapToDto(rs));
            }
            logger.fine("Found " + users.size() + " users");
            return users;
        } catch (SQLException e) {
            logger.severe("Failed to find all users: " + e.getMessage());
            throw e;
        }
    }

    public boolean update(UserDto user) throws SQLException {
        logger.info("Updating user ID: " + user.getUserId());
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setLong(4, user.getUserId());
            boolean result = stmt.executeUpdate() > 0;
            logger.info("User update " + (result ? "successful" : "failed"));
            return result;
        } catch (SQLException e) {
            logger.severe("Failed to update user ID " + user.getUserId() + ": " + e.getMessage());
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

    private UserDto mapToDto(ResultSet rs) throws SQLException {
        return new UserDto(
                rs.getLong("user_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}