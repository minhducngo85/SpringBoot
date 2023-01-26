package com.minhduc.tuto.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minhduc.tuto.controller.service.DemoService;
import com.minhduc.tuto.model.Customer;
import com.minhduc.tuto.model.MessageResponse;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api")
public class DemoController {
    
    private static final Logger LOGGER = LogManager.getLogger(DemoController.class);

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
	LOGGER.debug("Admin endpoint!");
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
	LOGGER.debug("Dashboard endpoint!");
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
	LOGGER.debug("Manager endpoint!");
	return new MessageResponse("Hello From Manager! Msg was sent from backend!");
    }

    @GetMapping("/customers")
    @RolesAllowed("ROLE_ADMIN")
    public List<Customer> getCustomers() {
	LOGGER.debug("Customer endpoint!");
	return service.getCustomers();
    }
}
