package com.example.wydad.services.impl;

import com.example.wydad.entities.News;
import com.example.wydad.entities.Product;
import com.example.wydad.repositories.ProductRepository;
import com.example.wydad.services.ProductService;
import com.example.wydad.web.exceptions.InvalidImageException;
import com.example.wydad.web.exceptions.InvalidProductException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Value("${file.upload.directory:uploads/images}")
    private String uploadDirectory;

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product, MultipartFile coverImage) throws IOException {
        if (product == null) {
            throw new InvalidProductException("Product object cannot be null");
        }

        if (product.getId() != null) {
            throw new InvalidProductException("Cannot save product with existing ID. Use update method instead.");
        }
        

        if (coverImage != null && !coverImage.isEmpty()) {
            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalFilename = coverImage.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new InvalidImageException("Cover image filename is missing");
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            Path filePath = Paths.get(uploadDirectory, newFilename);
            Files.write(filePath, coverImage.getBytes());

            product.setPicture("/images/" + newFilename);
        } else {
            throw new InvalidImageException("Cover image file is required");
        }

        return productRepository.save(product);
    }


    @Override
    public void deleteProduct(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent() && product.get().getPicture() != null) {
            String imagePath = product.get().getPicture();
            if (imagePath.startsWith("/images/")) {
                String filename = imagePath.substring("/images/".length());
                Path filePath = Paths.get(uploadDirectory, filename);
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    System.err.println("Failed to delete image file: " + e.getMessage());
                }
            }
        }
        productRepository.deleteById(id);
    }
}