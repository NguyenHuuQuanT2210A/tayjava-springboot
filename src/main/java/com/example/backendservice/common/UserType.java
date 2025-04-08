package com.example.backendservice.common;

public enum UserType {
    OWNER, ADMIN, USER;

    public static UserType fromString(String type) {
        for (UserType userType : UserType.values()) {
            if (userType.name().equalsIgnoreCase(type)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Unknown user type: " + type);
    }
}
