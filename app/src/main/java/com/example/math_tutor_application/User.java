package com.example.math_tutor_application;



public class User  {
    //an object to represent Users. Will contain thier userId, names and passwords.

    private String userId;
    private String name;
    private String password;

    private String role; // stores the role of the user such as student or tutor

    public User(String userId, String password, String name, String role) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        if (!role.equals("student") && !role.equals("tutor")) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
