package com.minhduc.tuto.springboot.keycloak.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.keycloak.adapters.springsecurity.account.KeycloakRole;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

public class CustomKeycloakAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomKeycloakAuthenticationProvider.class.getName());

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
	if ("minhducngo".equalsIgnoreCase(name) || "admin".equalsIgnoreCase(name)) {
	    grantedAuthorities.add(new KeycloakRole("admin"));
	}
	/// default role
	LOGGER.info("user {} logged successfully!", name);
	grantedAuthorities.add(new KeycloakRole("user"));
	for (String role : token.getAccount().getRoles()) {
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
