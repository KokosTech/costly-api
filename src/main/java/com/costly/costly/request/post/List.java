package com.costly.costly.request.post;

public class List {
    private String name;
    private String color;
    private String icon;
    private String userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "List{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", icon='" + icon + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
