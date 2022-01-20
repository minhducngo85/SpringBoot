package com.minhduc.tuto.springboot.keycloak.config;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.QueryParamPresenceRequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@Order(2)
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
class KeycloackSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    // Submits the KeycloakAuthenticationProvider to the AuthenticationManager
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	CustomKeycloakAuthenticationProvider keycloakAuthenticationProvider = new CustomKeycloakAuthenticationProvider();
	keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
	auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
	return new KeycloakSpringBootConfigResolver();
    }

    // Specifies the session authentication strategy
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
	return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	super.configure(http);
	http.authorizeRequests().antMatchers("/customers*", "/users*").hasRole("user")
	        // Admin user
	        .antMatchers("/admin*").hasAnyRole("admin", "super_admin")
	        //
	        .anyRequest().permitAll()
	        //
	        .and().cors().and().csrf().disable();
    }

    @Bean
    @Override
    protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
	RequestMatcher requestMatcher = new OrRequestMatcher(new AntPathRequestMatcher(KeycloakAuthenticationEntryPoint.DEFAULT_LOGIN_URI),
	        new QueryParamPresenceRequestMatcher(OAuth2Constants.ACCESS_TOKEN),
	        // We're providing our own authorization header matcher
	        new IgnoreKeycloakProcessingFilterRequestMatcher());
	return new KeycloakAuthenticationProcessingFilter(authenticationManagerBean(), requestMatcher);
    }

    // Matches request with Authorization header which value doesn't start with
    // "Basic " prefix
    private class IgnoreKeycloakProcessingFilterRequestMatcher implements RequestMatcher {
	IgnoreKeycloakProcessingFilterRequestMatcher() {
	}

	@Override
	public boolean matches(HttpServletRequest request) {
	    String authorizationHeaderValue = request.getHeader("Authorization");
	    return authorizationHeaderValue != null && !authorizationHeaderValue.startsWith("Basic ");
	}
    }
}
