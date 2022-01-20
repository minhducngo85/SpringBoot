package com.minhduc.tuto.springboot.keycloak.rest;

public class NotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -6769338647137396248L;

    public NotFoundException() {
	this("Product not found!");
    }

    public NotFoundException(String message) {
	super(message);
    }
}
