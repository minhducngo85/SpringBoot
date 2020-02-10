package com.minhduc.tuto.springboot.bootstrapping.restfull;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhduc.tuto.springboot.bootstrapping.restfull.model.Product;

/**
 * 
 * @author Minh Duc Ngo
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc // auto initialize Spring Web Application Context
public class ProductServiceControllerTests {

    @Autowired
    private MockMvc mvc;

    protected String mapToJson(Object obj) throws JsonProcessingException {
	ObjectMapper objectMapper = new ObjectMapper();
	return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
	ObjectMapper objectMapper = new ObjectMapper();
	return objectMapper.readValue(json, clazz);
    }

    @Test
    public void testGetProductsList() throws Exception {
	System.out.println("testGetProductsList");
	// action
	String uri = "/rest/products";
	MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON)
	        .accept(MediaType.APPLICATION_JSON);
	MvcResult mvcResult = mvc.perform(request).andReturn();
	int status = mvcResult.getResponse().getStatus();

	// Verify
	assertEquals(200, status);
	String content = mvcResult.getResponse().getContentAsString();
	@SuppressWarnings("unchecked")
	List<Product> productlist = mapFromJson(content, List.class);
	System.out.println("Product list = " + productlist.toString());
	assertTrue(productlist.size() > 0);
    }

    @Test
    public void testUpdateProductAndReturnStatusCodeNotFound() throws Exception {
	System.out.println("testUpdateProductAndReturnStatusCodeNotFound");
	// action
	Product product = new Product();
	product.setName("Lemon");
	String uri = "/rest/products/5";
	MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(mapToJson(product))
	        .accept(MediaType.APPLICATION_JSON);
	MvcResult mvcResult = mvc.perform(request).andReturn();
	int status = mvcResult.getResponse().getStatus();

	// Verify
	assertEquals(HttpStatus.NOT_FOUND.value(), status);
	String content = mvcResult.getResponse().getContentAsString();
	assertTrue(content.contains("not found!"));
    }

    @Test
    public void testUpdateProductAndReturnStatusCode200() throws Exception {
	System.out.println("testUpdateProductAndReturnStatusCode200");
	// action
	Product product = new Product();
	product.setName("Lemon");
	String uri = "/rest/products/2";
	MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).content(mapToJson(product))
	        .accept(MediaType.APPLICATION_JSON);
	MvcResult mvcResult = mvc.perform(request).andReturn();
	int status = mvcResult.getResponse().getStatus();

	// Verify
	assertEquals(HttpStatus.OK.value(), status);
	String content = mvcResult.getResponse().getContentAsString();
	assertEquals("Product is updated successsfully", content);
    }

    @Test
    public void testDeleteProduct() throws Exception {
	String uri = "/rest/products/2";
	MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
	int status = mvcResult.getResponse().getStatus();
	assertEquals(200, status);
	String content = mvcResult.getResponse().getContentAsString();
	assertEquals(content, "Product is deleted successsfully");
    }

    @Test
    public void testCreateProduct() throws Exception {
	String uri = "/rest/products";
	Product product = new Product();
	product.setId("3");
	product.setName("Ginger");
	String inputJson = mapToJson(product);
	MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
	        .andReturn();
	int status = mvcResult.getResponse().getStatus();
	assertEquals(201, status);
	String content = mvcResult.getResponse().getContentAsString();
	assertEquals(content, mapToJson(product));
    }
}
