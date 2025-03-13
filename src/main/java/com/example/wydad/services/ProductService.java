package com.example.wydad.services;

import com.example.wydad.entities.Player;
import com.example.wydad.entities.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Integer id);
    Product saveProduct(Product product, MultipartFile image) throws IOException;
    void deleteProduct(Integer id);
}