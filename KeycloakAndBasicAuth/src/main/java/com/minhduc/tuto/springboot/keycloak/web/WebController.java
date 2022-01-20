package com.minhduc.tuto.springboot.keycloak.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class WebController {

    @Autowired
    private CustomerDAO customerDAO;

    @GetMapping(path = "/")
    public String index() {
	return "external";
    }

    @GetMapping(path = "/denied")
    public String accessDenied() {
	return "accessdenied";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws Exception {
	request.logout();
	HttpSession session = request.getSession(false);
	// Destroys the session for this user.
	if (session != null) {
	    session.invalidate();
	}
	return "redirect:/";
    }

    @GetMapping(path = "/customers")
    public String customers(Principal principal, Model model) {
	Iterable<Customer> customers = customerDAO.findAll();
	int size = (int) StreamSupport.stream(customers.spliterator(), false).count();
	if (size < 15) {
	    addCustomers();
	    customers = customerDAO.findAll();
	}
	model.addAttribute("customers", customers);
	model.addAttribute("username", principal.getName());
	return "customers";
    }

    // add customers for demonstration
    public void addCustomers() {

	Customer customer1 = new Customer();
	customer1.setAddress("1111 foo blvd");
	customer1.setName("Foo Industries");
	customer1.setServiceRendered("Important services");
	customerDAO.save(customer1);

	Customer customer2 = new Customer();
	customer2.setAddress("2222 bar street");
	customer2.setName("Bar LLP");
	customer2.setServiceRendered("Important services");
	customerDAO.save(customer2);

	Customer customer3 = new Customer();
	customer3.setAddress("33 main street");
	customer3.setName("Big LLC");
	customer3.setServiceRendered("Important services");
	customerDAO.save(customer3);
    }
}
