package com.minhduc.tuto.springboot.keycloak.rest;

import java.util.Collection;


public interface IProductService {
    public abstract void createProduct(Product product);
    public abstract void updateProduct(String id, Product product);
    public abstract void deleteProduct(String id);
    public abstract boolean containProduct(String id);
    public abstract Collection<Product> getProducts();
}
