package com.mariiapasichna.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebAuthenticationManager implements AuthenticationManager {

    private List<AuthenticationProvider> providers;

    @Autowired
    public void setProviders(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        Authentication webAuthentication;
        for (AuthenticationProvider provider : providers) {
            webAuthentication = provider.authenticate(authentication);
            if (webAuthentication != null) {
                webAuthentication.setAuthenticated(true);
                return webAuthentication;
            }
        }
        throw new BadCredentialsException("Bad credentials!");
    }
}