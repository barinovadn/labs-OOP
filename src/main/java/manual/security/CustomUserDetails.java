package manual.security;

import manual.entity.UserEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CustomUserDetails {
    private static final Logger logger = Logger.getLogger(CustomUserDetails.class.getName());

    private final UserEntity userEntity;
    private final List<Role> roles;

    public CustomUserDetails(UserEntity userEntity, List<Role> roles) {
        this.userEntity = userEntity;
        this.roles = roles != null ? roles : Collections.emptyList();
        logger.fine("CustomUserDetails created for user: " + (userEntity != null ? userEntity.getUsername() : "null"));
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public Long getUserId() {
        return userEntity != null ? userEntity.getUserId() : null;
    }

    public String getEmail() {
        return userEntity != null ? userEntity.getEmail() : null;
    }

    public Collection<String> getAuthorities() {
        return roles.stream()
                .map(role -> "ROLE_" + role.name())
                .collect(Collectors.toList());
    }

    public String getPassword() {
        return userEntity != null ? userEntity.getPassword() : null;
    }

    public String getUsername() {
        return userEntity != null ? userEntity.getUsername() : null;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public boolean hasAnyRole(Role... checkRoles) {
        for (Role role : checkRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        if (getUserId() == null || that.getUserId() == null) {
            return false;
        }
        return Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }

    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "userId=" + getUserId() +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", authorities=" + getAuthorities() +
                '}';
    }
}

