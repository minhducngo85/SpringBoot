package com.minhduc.tuto.springboot.https;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MyRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyRestController.class.getName());
    
    @Value("${spring.application.name:HttpsApp}")
    private String name;

    @RequestMapping(value = "/")
    public String hello() {
	LOGGER.debug("Hello from " + name);
	return "Hello World from " + name;
    }
}
