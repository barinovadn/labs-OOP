package service;

import dto.UserRequest;
import dto.UserResponse;
import entity.UserEntity;
import repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
@Transactional
public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;

    public UserResponse createUser(UserRequest request) {
        logger.info("Creating user: " + request.getUsername());
        UserEntity user = new UserEntity(request.getUsername(), request.getPassword(), request.getEmail());
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
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        
        user = userRepository.save(user);
        logger.info("User updated: " + id);
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

