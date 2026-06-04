package com.app.ecom.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.model.Product;
import com.app.ecom.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    //TO create a new product
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    //get all products
    public List<ProductResponse> getAllProducts() {
        return productRepository.findByIsActiveTrue()
                                .stream()
                                .map(this::mapToProductResponse)
                                .collect(Collectors.toList());
    }

    //search products by keyword
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword)
                                .stream()
                                .map(this::mapToProductResponse)
                                .collect(Collectors.toList());
    }

    //To update an existing product
    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                                  .map(existingProduct -> {
                                      updateProductFromRequest(existingProduct, productRequest);
                                      Product updatedProduct = productRepository.save(existingProduct);
                                      return mapToProductResponse(updatedProduct);
                                  });
        
    }

    //To delete a product (soft delete)
    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                                .map(product -> {
                                        product.setIsActive(false);
                                        productRepository.save(product);
                                        return true;
                                        }).orElse(false);
    }

    //DTO Mapping methods

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
    }

    private ProductResponse mapToProductResponse(Product savedProduct) {
        ProductResponse response = new ProductResponse();
        response.setId(savedProduct.getId());
        response.setName(savedProduct.getName());
        response.setIsActive(savedProduct.getIsActive());
        response.setDescription(savedProduct.getDescription());
        response.setPrice(savedProduct.getPrice());
        response.setStockQuantity(savedProduct.getStockQuantity());
        response.setCategory(savedProduct.getCategory());
        response.setImageUrl(savedProduct.getImageUrl());
        return response;
    }
    
}
