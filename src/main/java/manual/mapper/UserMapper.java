package manual.mapper;

import manual.entity.UserEntity;
import manual.dto.CreateUserRequest;
import manual.dto.UserResponse;
import java.util.logging.Logger;

public class UserMapper {
    private static final Logger logger = Logger.getLogger(UserMapper.class.getName());

    public static UserEntity toEntity(CreateUserRequest request) {
        logger.fine("Mapping CreateUserRequest to UserEntity: " + request.getUsername());
        return new UserEntity(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()
        );
    }

    public static UserResponse toResponse(UserEntity entity) {
        logger.fine("Mapping UserEntity to UserResponse: " + entity.getUserId());
        return new UserResponse(
                entity.getUserId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getCreatedAt()
        );
    }
}