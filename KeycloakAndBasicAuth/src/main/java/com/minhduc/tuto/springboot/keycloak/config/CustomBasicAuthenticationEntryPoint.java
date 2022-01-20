package com.minhduc.tuto.springboot.keycloak.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

/**
 * BasicAuthenticationEntryPoint for RestApi
 * 
 * @author UE1PHOT
 *
 */

public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException)
            throws IOException {
	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
	PrintWriter writer = response.getWriter();
	try {
	    writer.println("HTTP Status 401 : " + authException.getMessage());
	} finally {
	    writer.close();
	}
    }

    @Override
    public void afterPropertiesSet() {
	setRealmName("BASICREALM");
	super.afterPropertiesSet();
    }
}