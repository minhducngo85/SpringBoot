package com.minhduc.tuto.springboot.keycloak.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.keycloak.adapters.springsecurity.account.KeycloakRole;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

public class CustomKeycloakAuthenticationProvider implements AuthenticationProvider {
    private GrantedAuthoritiesMapper grantedAuthoritiesMapper;

    public void setGrantedAuthoritiesMapper(GrantedAuthoritiesMapper grantedAuthoritiesMapper) {
	this.grantedAuthoritiesMapper = grantedAuthoritiesMapper;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
	List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
	// add custom role form Code
	String name = token.getName();
	System.out.println(name);
	if ("minhducngo".equalsIgnoreCase(name)) {
	    grantedAuthorities.add(new KeycloakRole("admin"));
	    grantedAuthorities.add(new KeycloakRole("rest"));
	} else if ("rest".equalsIgnoreCase(name)) {
	    grantedAuthorities.add(new KeycloakRole("rest"));
	}
	for (String role : token.getAccount().getRoles()) {
	    System.out.println(role);
	    grantedAuthorities.add(new KeycloakRole(role));
	}

	return new KeycloakAuthenticationToken(token.getAccount(), token.isInteractive(), mapAuthorities(grantedAuthorities));
    }

    private Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
	return grantedAuthoritiesMapper != null ? grantedAuthoritiesMapper.mapAuthorities(authorities) : authorities;
    }

    @Override
    public boolean supports(Class<?> aClass) {
	return KeycloakAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
