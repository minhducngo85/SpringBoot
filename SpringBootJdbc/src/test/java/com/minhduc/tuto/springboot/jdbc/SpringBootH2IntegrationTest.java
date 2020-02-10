package com.minhduc.tuto.springboot.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.minhduc.tuto.springboot.jdbc.dao.UserRepository;
import com.minhduc.tuto.springboot.jdbc.model.User;

@SpringBootTest(classes = SpringBootJdbcApplication.class)
@ExtendWith(SpringExtension.class)
public class SpringBootH2IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
	System.out.println("Context loaded");
    }

    @Test
    public void givenUserProfile_whenAddUser_thenCreateNewUser() {
	User user = new User();
	user.setFirstName("John");
	user.setLastName("Doe");
	userRepository.save(user);
	List<User> users = (List<User>) userRepository.findAll();
	assertFalse(users.isEmpty());

	String firstName = "Aliko";
	String lastName = "Dangote";
	User user1 = userRepository.findById(users.get(0).getId()).get();
	user1.setLastName(lastName);
	user1.setFirstName(firstName);
	userRepository.save(user1);

	user = userRepository.findById(user.getId()).get();
	assertEquals(user.getFirstName(), firstName);
	assertEquals(user.getLastName(), lastName);

	users = (List<User>) userRepository.findAll();
	users.forEach(anUser -> {
	    System.out.println(anUser);
	});

	userRepository.deleteById(user.getId());
	assertTrue(((List<User>) userRepository.findAll()).isEmpty());
    }
}
