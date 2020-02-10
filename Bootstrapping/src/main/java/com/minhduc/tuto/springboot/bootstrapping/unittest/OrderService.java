package com.minhduc.tuto.springboot.bootstrapping.unittest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private CategoryService categoryService;

    public String getCategoryName() {
	return categoryService.getCategoryName();
    }
}
