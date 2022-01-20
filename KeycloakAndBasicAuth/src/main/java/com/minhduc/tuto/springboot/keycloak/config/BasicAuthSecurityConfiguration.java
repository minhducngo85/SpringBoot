package com.minhduc.tuto.springboot.keycloak.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Order(1)
public class BasicAuthSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.basic.matcher:/rest/**,/rest*}")
    String endpointMatcher;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
	return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.inMemoryAuthentication().passwordEncoder(passwordEncoder) //
	        .withUser("rest").password(passwordEncoder.encode("rest")).roles("REST") //
	        .and() //
	        .withUser("admin").password(passwordEncoder.encode("admin")).roles("REST", "USER", "ADMIN") //
	        .and() //
	        .withUser("user").password(passwordEncoder.encode("user")).roles("USER"); //
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.cors().and().csrf().disable();
	// http.requestMatchers().antMatchers(endpointMatcher.split(",")).and().authorizeRequests().anyRequest().authenticated().and().httpBasic();
	http.requestMatchers().antMatchers(endpointMatcher.split(",")).and().authorizeRequests().anyRequest().hasRole("REST").and().httpBasic();
    }
}
