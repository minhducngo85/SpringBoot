package com.minhduc.tuto.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minhduc.tuto.model.Student;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/students")
public class StudentController {
    private static final Logger LOGGER = LogManager.getLogger(StudentController.class);
    
    private List<Student> someStudents = List.of(
            new Student(1, "Kevin"),
            new Student(2, "Lucas"),
            new Student(3, "Mia Ngo"));

    @GetMapping
    @RolesAllowed({"ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN"})
    public List<Student> students() {
	LOGGER.debug("get students");
        return someStudents;
    }

    @GetMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public Student getStudent(@PathVariable("id") String id) {
	LOGGER.debug("get students with id = " + id);
	return someStudents.stream()
                .filter(s -> Integer.toString(s.getId()).equals(id))
                .findFirst()
                .orElse(null);
    }
}

