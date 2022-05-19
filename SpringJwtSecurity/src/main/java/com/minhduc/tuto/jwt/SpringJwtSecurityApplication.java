package com.minhduc.tuto.jwt;

import java.util.HashSet;
import java.util.Set;

import com.minhduc.tuto.jwt.model.ERole;
import com.minhduc.tuto.jwt.model.Role;
import com.minhduc.tuto.jwt.model.User;
import com.minhduc.tuto.jwt.repository.RoleRepository;
import com.minhduc.tuto.jwt.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringJwtSecurityApplication {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
	SpringApplication.run(SpringJwtSecurityApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
	return args -> {
	    roleRepository.save(new Role(ERole.ROLE_USER));
	    roleRepository.save(new Role(ERole.ROLE_MODERATOR));
	    roleRepository.save(new Role(ERole.ROLE_ADMIN));

	    User aUser = new User("admin", "admin@minhduc-tuto.com", encoder.encode("admin"));
	    Set<Role> roles = new HashSet<>();
	    Role aRole = roleRepository.findByName(ERole.ROLE_ADMIN)
		    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	    roles.add(aRole);
	    Role aRole2 = roleRepository.findByName(ERole.ROLE_MODERATOR)
		    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	    roles.add(aRole2);
	    aUser.setRoles(roles);
	    userRepository.save(aUser);

	    aUser = new User("moderator", "moderator@minhduc-tuto.com", encoder.encode("moderator"));
	    roles = new HashSet<>();
	    aRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
		    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	    roles.add(aRole);
	    aUser.setRoles(roles);
	    userRepository.save(aUser);

	    aUser = new User("user", "user@minhduc-tuto.com", encoder.encode("user"));
	    roles = new HashSet<>();
	    aRole = roleRepository.findByName(ERole.ROLE_USER)
		    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	    roles.add(aRole);
	    aUser.setRoles(roles);
	    userRepository.save(aUser);
	};
    }

}
