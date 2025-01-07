package com.example.wydad.services;

import com.example.wydad.entities.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Integer id);
    Product saveProduct(Product product);
    void deleteProduct(Integer id);
}