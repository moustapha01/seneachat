package com.signaretech.seneachat.service;

import com.signaretech.seneachat.model.AuthenticationResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SignareAuthenticationManager implements AuthenticationManager {

    private final IUserService userService;
    private static final List<GrantedAuthority> authorities = new ArrayList<>();


    static {
        authorities.add(new SimpleGrantedAuthority("SELLER"));
    }

    public SignareAuthenticationManager(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws BadCredentialsException {

        AuthenticationResult authResult = userService.authenticateUser(authentication.getName(),
                authentication.getCredentials().toString());

        if(authResult.isAuthenticated()){
            return new UsernamePasswordAuthenticationToken(authentication.getName(),
                    authentication.getCredentials(), authorities);
        }

        throw new BadCredentialsException(authResult.getError());
    }
}
