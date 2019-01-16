package com.programasoft.drinkshopserver.Model;

public class token {
    private String Phone;
    private String Token;
    private boolean IsServerToken;

    public token() {
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public boolean isServerToken() {
        return IsServerToken;
    }

    public void setServerToken(boolean serverToken) {
        IsServerToken = serverToken;
    }
}
