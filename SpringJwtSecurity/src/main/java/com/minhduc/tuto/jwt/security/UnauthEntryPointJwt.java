package com.minhduc.tuto.jwt.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * class that implements AuthenticationEntryPoint interface. Then we override
 * the commence() method. This method will be triggerd anytime unauthenticated
 * User requests a secured HTTP resource and an AuthenticationException is
 * thrown.
 */
@Component
public class UnauthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(UnauthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
	logger.error("Unauthorized error: {}", authException.getMessage());

	response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

	final Map<String, Object> body = new HashMap<>();
	body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
	body.put("error", "Unauthorized");
	body.put("message", authException.getMessage());
	body.put("path", request.getServletPath());

	final ObjectMapper mapper = new ObjectMapper();
	mapper.writeValue(response.getOutputStream(), body);

    }

}
