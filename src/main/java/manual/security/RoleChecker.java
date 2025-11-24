package manual.security;

import java.util.List;
import java.util.logging.Logger;

public class RoleChecker {
    private static final Logger logger = Logger.getLogger(RoleChecker.class.getName());

    public static boolean hasAnyRole(SecurityContext context, Role... requiredRoles) {
        if (context == null || !context.isAuthenticated()) {
            logger.warning("Security context is null or user not authenticated");
            return false;
        }

        List<Role> userRoles = context.getRoles();
        if (userRoles == null || userRoles.isEmpty()) {
            logger.warning("User has no roles");
            return false;
        }

        for (Role requiredRole : requiredRoles) {
            if (userRoles.contains(requiredRole)) {
                logger.fine("User has required role: " + requiredRole);
                return true;
            }
        }

        logger.warning("User does not have any of required roles");
        return false;
    }

    public static boolean hasRole(SecurityContext context, Role requiredRole) {
        return hasAnyRole(context, requiredRole);
    }

    public static boolean isOwnerOrAdmin(SecurityContext context, Long resourceUserId) {
        if (context == null || !context.isAuthenticated()) {
            return false;
        }

        if (hasRole(context, Role.ADMIN)) {
            logger.fine("Admin access granted");
            return true;
        }

        Long currentUserId = context.getUserId();
        boolean isOwner = currentUserId != null && currentUserId.equals(resourceUserId);
        logger.fine("Owner check: " + isOwner + " (current: " + currentUserId + ", resource: " + resourceUserId + ")");
        return isOwner;
    }

    public static boolean canReadAll(SecurityContext context) {
        return hasAnyRole(context, Role.ADMIN, Role.OPERATOR);
    }

    public static boolean canWriteAll(SecurityContext context) {
        return hasRole(context, Role.ADMIN);
    }
}


