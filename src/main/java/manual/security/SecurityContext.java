package manual.security;

import manual.entity.UserEntity;
import java.util.List;

public class SecurityContext {
    private final CustomUserDetails userDetails;

    public SecurityContext(UserEntity user, List<Role> roles) {
        this.userDetails = new CustomUserDetails(user, roles);
    }

    public UserEntity getUser() {
        return userDetails.getUserEntity();
    }

    public List<Role> getRoles() {
        return userDetails.getRoles();
    }

    public boolean hasRole(Role role) {
        return userDetails.hasRole(role);
    }

    public boolean hasAnyRole(Role... roles) {
        return userDetails.hasAnyRole(roles);
    }

    public boolean isAuthenticated() {
        return userDetails.getUserEntity() != null;
    }

    public Long getUserId() {
        return userDetails.getUserId();
    }

    public CustomUserDetails getUserDetails() {
        return userDetails;
    }

    public String getUsername() {
        return userDetails.getUsername();
    }
}


