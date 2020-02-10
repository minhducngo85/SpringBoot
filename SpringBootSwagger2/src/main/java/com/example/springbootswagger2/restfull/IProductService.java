package com.example.springbootswagger2.restfull;

import java.util.Collection;

import com.example.springbootswagger2.model.Product;


public interface IProductService {
    public abstract void createProduct(Product product);
    public abstract void updateProduct(String id, Product product);
    public abstract void deleteProduct(String id);
    public abstract boolean containProduct(String id);
    public abstract Collection<Product> getProducts();
}
