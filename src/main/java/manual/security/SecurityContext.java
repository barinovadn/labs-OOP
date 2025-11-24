package manual.security;

import manual.entity.UserEntity;
import java.util.List;

public class SecurityContext {
    private UserEntity user;
    private List<Role> roles;

    public SecurityContext(UserEntity user, List<Role> roles) {
        this.user = user;
        this.roles = roles;
    }

    public UserEntity getUser() {
        return user;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public boolean hasRole(Role role) {
        return roles != null && roles.contains(role);
    }

    public boolean isAuthenticated() {
        return user != null;
    }

    public Long getUserId() {
        return user != null ? user.getUserId() : null;
    }
}


