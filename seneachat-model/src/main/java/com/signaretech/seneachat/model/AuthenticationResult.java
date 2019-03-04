package com.signaretech.seneachat.model;

public class AuthenticationResult {

    private boolean authenticated;
    private String error;
    private final String sellerStatus;

    public AuthenticationResult(String sellerStatus) {
        this.sellerStatus = sellerStatus;
    }

    public AuthenticationResult(boolean authenticated, String error, String sellerStatus) {
        this.authenticated = authenticated;
        this.error = error;
        this.sellerStatus = sellerStatus;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSellerStatus() {
        return sellerStatus;
    }
}
