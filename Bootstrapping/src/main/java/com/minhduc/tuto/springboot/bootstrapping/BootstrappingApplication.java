package com.minhduc.tuto.springboot.bootstrapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
// @EnableScheduling // enable scheduling
public class BootstrappingApplication {

    /** Logger object */
    private static final Logger LOGGER = LoggerFactory.getLogger(BootstrappingApplication.class.getName());

    
    @Value("${spring.application.name}")
    private String name;

    public static void main(String[] args) {
	LOGGER.info("this is a info message");
	LOGGER.warn("this is a warn message");
	LOGGER.error("this is a error message");
	LOGGER.error("this is a custom message");
	LOGGER.error("this is a custom message 2");
	SpringApplication.run(BootstrappingApplication.class, args);
    }

    @RequestMapping(value = "/appname")
    public String name() {
	return name;
    }

}
