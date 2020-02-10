package com.minhduc.tuto.springboot.bootstrapping.restfull;

import java.util.Collection;

import com.minhduc.tuto.springboot.bootstrapping.restfull.model.Product;

public interface IProductService {
    public abstract void createProduct(Product product);
    public abstract void updateProduct(String id, Product product);
    public abstract void deleteProduct(String id);
    public abstract boolean containProduct(String id);
    public abstract Collection<Product> getProducts();
}
