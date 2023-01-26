package com.minhduc.tuto.controller.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.minhduc.tuto.model.Customer;

@Service
public class DemoService {

    private List<Customer> customers = List.of(
            new Customer(1, "Minh", 18),
            new Customer(2, "Mia" ,30),
            new Customer(3, "Pieter", 50));
    
    public List<Customer> getCustomers(){
	return customers;
    }
}
