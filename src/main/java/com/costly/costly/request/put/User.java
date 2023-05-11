package com.costly.costly.request.put;

public class User {
    private String id;
    private String password;
    private String name = null; // optional if logging in

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
