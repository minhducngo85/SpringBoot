package com.minhduc.tuto.springboot.thymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Use the following code to create a @Controller class file to redirect the
 * Request URI to HTML file
 *
 */
@Controller
public class ViewController {

    @RequestMapping(value = "/index")
    public String index() {
	return "index";
    }

    @RequestMapping("/locale")
    public String locale() {
	return "locale";
    }

    @RequestMapping("/view-products")
    public String viewProducts() {
	return "view-products";
    }

    @RequestMapping("/add-product")
    public String addProducts() {
	return "add-product";
    }
}
