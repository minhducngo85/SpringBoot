package com.minhduc.tuto.springboot.bootstrapping.unittest;

import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    public String getCategoryName() {
	return "Tablet";
    }
}
