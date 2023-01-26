package com.example.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.model.Student;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private List<Student> someStudents = List.of(
            new Student(1, "Kevin"),
            new Student(2, "Lucas"),
            new Student(3, "Mia Ngo"));

    @GetMapping
    @RolesAllowed({"ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN"})
    public List<Student> students() {
	System.out.println("get students");
        return someStudents;
    }

    @GetMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public Student getStudent(@PathVariable("id") String id) {
	System.out.println("get students with id = " + id);
	return someStudents.stream()
                .filter(s -> Integer.toString(s.getId()).equals(id))
                .findFirst()
                .orElse(null);
    }
}

