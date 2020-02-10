package com.minhduc.tuto.springboot.tomcatDeployment;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppRestController {
    @RequestMapping(value = "/")
    public String hello() {
	return "Hello World";
    }
}
