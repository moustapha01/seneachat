package com.signaretech.seneachat.service;

public interface SecurityService {

    String getLoggedInUser();

    void authenticateUser(String userName, String password);
}
