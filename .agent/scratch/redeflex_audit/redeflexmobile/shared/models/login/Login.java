package com.axys.redeflexmobile.shared.models.login;

public class Login {

    private final String uuid;
    private final String username;
    private final String password;

    public Login(String uuid, String username, String password) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
