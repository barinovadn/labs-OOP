package service;

import entity.UserEntity;
import repository.UserRepository;
import security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: " + username);
        
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warning("User not found: " + username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        CustomUserDetails userDetails = new CustomUserDetails(userEntity);
        
        logger.info("User loaded successfully: " + username + " with roles: " + 
                   userDetails.getAuthorities().stream()
                       .map(GrantedAuthority::getAuthority)
                       .collect(Collectors.joining(", ")));
        
        return userDetails;
    }
}

