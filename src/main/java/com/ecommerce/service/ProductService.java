package com.ecommerce.service;

import com.ecommerce.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Product Service Interface
 */
public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO getProductById(Long id);

    ProductDTO getProductBySku(String sku);

    List<ProductDTO> getAllProducts();

    Page<ProductDTO> getProducts(Pageable pageable);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    void deleteProduct(Long id);

    List<ProductDTO> getProductsByCategory(Long categoryId);

    List<ProductDTO> searchProducts(String keyword);
}
