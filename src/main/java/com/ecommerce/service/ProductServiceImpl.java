package com.ecommerce.service;

import com.ecommerce.dto.ProductDTO;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Product Service Implementation
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .sku(productDTO.getSku())
                .price(productDTO.getPrice())
                .costPrice(productDTO.getCostPrice())
                .quantity(productDTO.getQuantity())
                .categoryId(productDTO.getCategoryId())
                .imageUrl(productDTO.getImageUrl())
                .status(productDTO.getStatus() != null ? 
                    Product.ProductStatus.valueOf(productDTO.getStatus()) : 
                    Product.ProductStatus.ACTIVE)
                .rating(productDTO.getRating())
                .reviewCount(productDTO.getReviewCount())
                .isActive(productDTO.getIsActive() != null ? productDTO.getIsActive() : true)
                .build();

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return convertToDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "sku", sku));
        return convertToDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (productDTO.getName() != null) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            product.setDescription(productDTO.getDescription());
        }
        if (productDTO.getPrice() != null) {
            product.setPrice(productDTO.getPrice());
        }
        if (productDTO.getCostPrice() != null) {
            product.setCostPrice(productDTO.getCostPrice());
        }
        if (productDTO.getQuantity() != null) {
            product.setQuantity(productDTO.getQuantity());
        }
        if (productDTO.getCategoryId() != null) {
            product.setCategoryId(productDTO.getCategoryId());
        }
        if (productDTO.getImageUrl() != null) {
            product.setImageUrl(productDTO.getImageUrl());
        }
        if (productDTO.getStatus() != null) {
            product.setStatus(Product.ProductStatus.valueOf(productDTO.getStatus()));
        }
        if (productDTO.getIsActive() != null) {
            product.setIsActive(productDTO.getIsActive());
        }

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                keyword, keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .sku(product.getSku())
                .price(product.getPrice())
                .costPrice(product.getCostPrice())
                .quantity(product.getQuantity())
                .categoryId(product.getCategoryId())
                .imageUrl(product.getImageUrl())
                .status(product.getStatus() != null ? product.getStatus().name() : null)
                .rating(product.getRating())
                .reviewCount(product.getReviewCount())
                .isActive(product.getIsActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
