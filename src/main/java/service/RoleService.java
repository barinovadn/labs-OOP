package service;

import dto.RoleRequest;
import dto.RoleResponse;
import entity.RoleEntity;
import repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
@Transactional
public class RoleService {
    private static final Logger logger = Logger.getLogger(RoleService.class.getName());

    @Autowired
    private RoleRepository roleRepository;

    public RoleResponse createRole(RoleRequest request) {
        logger.info("Creating role: " + request.getRoleName());
        if (roleRepository.existsByRoleName(request.getRoleName())) {
            throw new RuntimeException("Role already exists: " + request.getRoleName());
        }
        
        RoleEntity role = new RoleEntity(request.getRoleName(), request.getDescription());
        role = roleRepository.save(role);
        logger.info("Role created with ID: " + role.getRoleId());
        return toResponse(role);
    }

    public RoleResponse getRoleById(Long id) {
        logger.info("Getting role by ID: " + id);
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        return toResponse(role);
    }

    public RoleResponse getRoleByName(String roleName) {
        logger.info("Getting role by name: " + roleName);
        RoleEntity role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));
        return toResponse(role);
    }

    public List<RoleResponse> getAllRoles() {
        logger.info("Getting all roles");
        return roleRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public RoleResponse updateRole(Long id, RoleRequest request) {
        logger.info("Updating role with ID: " + id);
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        
        role = roleRepository.save(role);
        logger.info("Role updated: " + id);
        return toResponse(role);
    }

    public void deleteRole(Long id) {
        logger.info("Deleting role with ID: " + id);
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
        logger.info("Role deleted: " + id);
    }

    private RoleResponse toResponse(RoleEntity role) {
        RoleResponse response = new RoleResponse();
        response.setRoleId(role.getRoleId());
        response.setRoleName(role.getRoleName());
        response.setDescription(role.getDescription());
        return response;
    }
}

