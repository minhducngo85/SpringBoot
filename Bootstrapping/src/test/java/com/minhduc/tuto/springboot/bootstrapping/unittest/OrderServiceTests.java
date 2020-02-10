package com.minhduc.tuto.springboot.bootstrapping.unittest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @Autowired
    private OrderService orderService;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void whenUserIdIsProvided_thenRetrievedNameIsCorrect() {
	Mockito.when(categoryService.getCategoryName()).thenReturn("Mock Category Name");
	String testName = orderService.getCategoryName();
	assertEquals("Mock Category Name", testName);
    }
}
