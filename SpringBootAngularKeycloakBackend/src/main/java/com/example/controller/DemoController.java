package com.example.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.controller.service.DemoService;
import com.example.model.Customer;
import com.example.model.MessageResponse;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api")
public class DemoController {

    @Autowired
    private DemoService service;

    /**
     * only for admin
     * 
     * @return
     */
    @GetMapping(value = "/admin")
    @RolesAllowed("ROLE_ADMIN")
    public MessageResponse adminEndPoint() {
	System.out.println("Admin endpoint!");
	return new MessageResponse("Hello From Admin! Msg was sent from backend!");
    }

    /**
     * for authenticated user
     * 
     * @return
     */
    @GetMapping(value = "/dashboard")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER" })
    public MessageResponse loggedUserEndPoint() {
	System.out.println("Dashboard endpoint!");
	return new MessageResponse("Hello From Dashboard! Msg was sent from backend!");
    }

    /**
     * for manager role
     * 
     * @return
     */
    @GetMapping(value = "/manager")
    @RolesAllowed("ROLE_MANAGER")
    public MessageResponse managerEndPoint() {
	System.out.println("Manager endpoint!");
	return new MessageResponse("Hello From Manager! Msg was sent from backend!");
    }

    @GetMapping("/customers")
    @RolesAllowed("ROLE_ADMIN")
    public List<Customer> getCustomers() {
	System.out.println("Customer endpoint!");
	return service.getCustomers();
    }
}
