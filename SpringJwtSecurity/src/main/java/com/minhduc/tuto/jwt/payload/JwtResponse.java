package com.minhduc.tuto.jwt.payload;

import java.util.Date;
import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private Date expiration;

    public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles, Date expiration) {
	this.token = accessToken;
	this.id = id;
	this.username = username;
	this.email = email;
	this.roles = roles;
	this.setExpiration(expiration);
    }

    public String getAccessToken() {
	return token;
    }

    public void setAccessToken(String accessToken) {
	this.token = accessToken;
    }

    public String getTokenType() {
	return type;
    }

    public void setTokenType(String tokenType) {
	this.type = tokenType;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public List<String> getRoles() {
	return roles;
    }

    /**
     * @return the expiration
     */
    public Date getExpiration() {
	return expiration;
	
    }

    /**
     * @param expiration the expiration to set
     */
    public void setExpiration(Date expiration) {
	this.expiration = expiration;
	
    }

  
}
