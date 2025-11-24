package manual.security;

public enum Role {
    ADMIN("ADMIN"),
    USER("USER"),
    OPERATOR("OPERATOR");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static Role fromString(String roleName) {
        if (roleName == null) {
            return null;
        }
        for (Role role : Role.values()) {
            if (role.roleName.equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        return null;
    }
}


