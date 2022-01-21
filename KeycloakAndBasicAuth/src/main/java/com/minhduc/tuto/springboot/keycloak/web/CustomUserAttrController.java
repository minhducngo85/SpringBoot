package com.minhduc.tuto.springboot.keycloak.web;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.IDToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomUserAttrController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserAttrController.class.getName());

    @GetMapping(path = "/users")
    public String getUserInfo(Model model) {
	LOGGER.info("get user info");
	getUserDetails(model);
	return "userInfo";
    }

    @GetMapping(path = "/admin")
    public String getAdminInfo(Model model) {
	model = getUserDetails(model);
	return "admin";
    }

    private Model getUserDetails(Model model) {
	KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
	final Principal principal = (Principal) authentication.getPrincipal();

	if (principal != null) {
	    // user has a valid session, we can assign role on the fly like this

	}
	String dob = "";
	if (principal instanceof KeycloakPrincipal) {
	    @SuppressWarnings("unchecked")
	    KeycloakPrincipal<KeycloakSecurityContext> kPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) principal;
	    IDToken token = kPrincipal.getKeycloakSecurityContext().getIdToken();
	    if (token != null) {
		Map<String, Object> customClaims = token.getOtherClaims();

		if (customClaims.containsKey("DOB")) {
		    dob = String.valueOf(customClaims.get("DOB"));
		}
		for (String key : customClaims.keySet()) {
		    LOGGER.info(key + " = " + customClaims.get(key));
		}
		if (kPrincipal.getKeycloakSecurityContext().getToken() != null
		        && kPrincipal.getKeycloakSecurityContext().getToken().getRealmAccess() != null) {
		    model.addAttribute("KeycloakRoles", kPrincipal.getKeycloakSecurityContext().getToken().getRealmAccess().getRoles());
		}
	    }
	}

	Set<String> roles = authentication.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());

	model.addAttribute("username", principal.getName());
	model.addAttribute("dob", dob);
	return model.addAttribute("AppRoles", roles);
    }

}
