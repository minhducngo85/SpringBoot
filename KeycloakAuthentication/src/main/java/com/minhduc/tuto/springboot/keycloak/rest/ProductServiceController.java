package com.minhduc.tuto.springboot.keycloak.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductServiceController {

    @Autowired
    private IProductService productService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceController.class.getName());

    @RequestMapping(value = "/rest/products/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
	LOGGER.info("delete product");
	productService.deleteProduct(id);
	return new ResponseEntity<>("Product is deleted successsfully", HttpStatus.OK);
    }

    /**
     * to update a product
     * 
     * @param id
     * @param product
     * @return
     */
    @RequestMapping(value = "/rest/products/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateProduct(@PathVariable("id") String id, @RequestBody Product product) {
	LOGGER.info("update product");
	if (!productService.containProduct(id)) {
	    throw new NotFoundException(String.format("Prodcut with given id = %s not found!", id));
	}
	productService.updateProduct(id, product);
	return new ResponseEntity<>("Product is updated successsfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/products", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createProduct(@RequestBody Product product) {
	LOGGER.info("add product");
	productService.createProduct(product);
	return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rest/products", method = RequestMethod.GET)
    public ResponseEntity<Object> getProduct() {
	LOGGER.info("get products");
	return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/products", method = RequestMethod.GET)
    public ResponseEntity<Object> getApiProduct() {
	LOGGER.info("get products");
	return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
    }
}
