package com.minhduc.tuto.springboot.keycloak.web;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.IDToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomUserAttrController {

    @SuppressWarnings("unchecked")
    @GetMapping(path = "/users")
    public String getUserInfo(Model model) {
	KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
	final Principal principal = (Principal) authentication.getPrincipal();

	if (principal != null) {
	    // user has a valid session, we can assign role on the fly like this

	}
	String dob = "";
	if (principal instanceof KeycloakPrincipal) {
	    KeycloakPrincipal<KeycloakSecurityContext> kPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) principal;
	    IDToken token = kPrincipal.getKeycloakSecurityContext().getIdToken();
	    Map<String, Object> customClaims = token.getOtherClaims();

	    if (customClaims.containsKey("DOB")) {
		dob = String.valueOf(customClaims.get("DOB"));
	    }
	    for (String key : customClaims.keySet()) {
		System.out.println(key + " = " + customClaims.get(key));
	    }
	    model.addAttribute("KeycloakRoles", kPrincipal.getKeycloakSecurityContext().getToken().getRealmAccess().getRoles());
	}

	Set<String> roles = authentication.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());

	model.addAttribute("username", principal.getName());
	model.addAttribute("dob", dob);
	model.addAttribute("AppRoles", roles);
	return "userInfo";
    }

    @SuppressWarnings("unchecked")
    @GetMapping(path = "/admin")
    public String getAdminInfo(Model model) {
	KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
	final Principal principal = (Principal) authentication.getPrincipal();

	if (principal != null) {
	    // user has a valid session, we can assign role on the fly like this

	}
	String dob = "";
	if (principal instanceof KeycloakPrincipal) {
	    KeycloakPrincipal<KeycloakSecurityContext> kPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) principal;
	    IDToken token = kPrincipal.getKeycloakSecurityContext().getIdToken();
	    Map<String, Object> customClaims = token.getOtherClaims();

	    if (customClaims.containsKey("DOB")) {
		dob = String.valueOf(customClaims.get("DOB"));
	    }
	    model.addAttribute("KeycloakRoles", kPrincipal.getKeycloakSecurityContext().getToken().getRealmAccess().getRoles());
	}

	Set<String> roles = authentication.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());

	model.addAttribute("username", principal.getName());
	model.addAttribute("dob", dob);
	model.addAttribute("AppRoles", roles);
	return "admin";
    }

}
