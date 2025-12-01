package security;

import entity.RoleEntity;
import entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    
    private final UserEntity userEntity;
    private final Collection<GrantedAuthority> authorities;

    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
        this.authorities = buildAuthorities(userEntity);
    }

    private Collection<GrantedAuthority> buildAuthorities(UserEntity user) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        
        for (RoleEntity role : user.getRoles()) {
            authorityList.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()));
        }
        
        // If user has no roles, give them USER role by default
        if (authorityList.isEmpty()) {
            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        
        return authorityList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public Long getUserId() {
        return userEntity.getUserId();
    }
    
    public String getEmail() {
        return userEntity.getEmail();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CustomUserDetails other = (CustomUserDetails) obj;
        return userEntity.getUserId() != null && 
               userEntity.getUserId().equals(other.userEntity.getUserId());
    }

    @Override
    public int hashCode() {
        return userEntity.getUserId() != null ? userEntity.getUserId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "userId=" + userEntity.getUserId() +
                ", username='" + userEntity.getUsername() + '\'' +
                ", email='" + userEntity.getEmail() + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}

