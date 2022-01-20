package com.minhduc.tuto.springboot.keycloak.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.minhduc.tuto.springboot.keycloak.rest.IProductService;

@RestController
public class RestApiController {

    @Autowired
    private IProductService productService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiController.class.getName());
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/rest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map> testController() {
        Map<String, String> result = new HashMap<>();
        result.put("msg", "it works");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/rest/products", method = RequestMethod.GET)
    public ResponseEntity<Object> getProduct() {
	LOGGER.info("get products");
	return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
    }

}