package manual.repository;

import manual.DatabaseConnection;
import manual.security.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RoleRepository {
    private static final Logger logger = Logger.getLogger(RoleRepository.class.getName());
    private Connection connection;

    public RoleRepository(Connection connection) {
        this.connection = connection;
        logger.info("RoleRepository initialized");
    }

    public List<Role> findRolesByUserId(Long userId) throws SQLException {
        logger.fine("Finding roles for user ID: " + userId);
        String sql = "SELECT r.role_name FROM roles r " +
                     "INNER JOIN user_roles ur ON r.role_id = ur.role_id " +
                     "WHERE ur.user_id = ?";
        
        List<Role> roles = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String roleName = rs.getString("role_name");
                Role role = Role.fromString(roleName);
                if (role != null) {
                    roles.add(role);
                }
            }
        }
        logger.fine("Found " + roles.size() + " roles for user " + userId);
        return roles;
    }

    public Long findRoleIdByName(String roleName) throws SQLException {
        logger.fine("Finding role ID for role name: " + roleName);
        String sql = "SELECT role_id FROM roles WHERE role_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("role_id");
            }
        }
        return null;
    }

    public boolean assignRoleToUser(Long userId, String roleName) throws SQLException {
        logger.info("Assigning role " + roleName + " to user ID: " + userId);
        
        Long roleId = findRoleIdByName(roleName);
        if (roleId == null) {
            logger.warning("Role not found: " + roleName);
            return false;
        }

        String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?) " +
                     "ON CONFLICT (user_id, role_id) DO NOTHING";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, roleId);
            int rowsAffected = stmt.executeUpdate();
            boolean result = rowsAffected > 0;
            logger.info("Role assignment " + (result ? "successful" : "already exists"));
            return result;
        }
    }

    public boolean removeRoleFromUser(Long userId, String roleName) throws SQLException {
        logger.info("Removing role " + roleName + " from user ID: " + userId);
        
        Long roleId = findRoleIdByName(roleName);
        if (roleId == null) {
            logger.warning("Role not found: " + roleName);
            return false;
        }

        String sql = "DELETE FROM user_roles WHERE user_id = ? AND role_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, roleId);
            int rowsAffected = stmt.executeUpdate();
            boolean result = rowsAffected > 0;
            logger.info("Role removal " + (result ? "successful" : "failed"));
            return result;
        }
    }

    public List<String> getAllRoleNames() throws SQLException {
        logger.fine("Getting all role names");
        String sql = "SELECT role_name FROM roles ORDER BY role_name";
        List<String> roleNames = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                roleNames.add(rs.getString("role_name"));
            }
        }
        return roleNames;
    }
}


