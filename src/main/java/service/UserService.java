package service;

import dto.AssignRoleRequest;
import dto.UserRequest;
import dto.UserResponse;
import entity.RoleEntity;
import entity.UserEntity;
import repository.RoleRepository;
import repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
@Transactional
public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserRequest request) {
        logger.info("Creating user: " + request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        
        // Assign default USER role if no roles specified
        if (user.getRoles().isEmpty()) {
            RoleEntity userRole = roleRepository.findByRoleName("USER")
                    .orElseGet(() -> {
                        logger.info("Creating default USER role");
                        RoleEntity newRole = new RoleEntity("USER", "Default user role");
                        return roleRepository.save(newRole);
                    });
            user.getRoles().add(userRole);
        }
        
        user = userRepository.save(user);
        logger.info("User created with ID: " + user.getUserId());
        return toResponse(user);
    }

    public UserResponse getUserById(Long id) {
        logger.info("Getting user by ID: " + id);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        logger.info("Getting all users");
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        logger.info("Updating user with ID: " + id);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setEmail(request.getEmail());
        
        user = userRepository.save(user);
        logger.info("User updated: " + id);
        return toResponse(user);
    }

    public UserResponse assignRoles(AssignRoleRequest request) {
        logger.info("Assigning roles to user ID: " + request.getUserId());
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
        
        List<RoleEntity> roles = new ArrayList<>();
        for (Long roleId : request.getRoleIds()) {
            RoleEntity role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
            roles.add(role);
        }
        
        user.setRoles(roles);
        user = userRepository.save(user);
        
        logger.info("Roles assigned to user ID: " + request.getUserId() + 
                   ", roles: " + roles.stream().map(RoleEntity::getRoleName).collect(Collectors.joining(", ")));
        return toResponse(user);
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: " + id);
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        logger.info("User deleted: " + id);
    }

    private UserResponse toResponse(UserEntity user) {
        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}

