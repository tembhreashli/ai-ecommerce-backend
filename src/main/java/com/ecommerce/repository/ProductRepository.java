package com.ecommerce.repository;

import com.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product entity.
 * Provides CRUD operations and custom query methods for product data access.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find a product by its name.
     *
     * @param name the product name
     * @return an Optional containing the product if found
     */
    Optional<Product> findByName(String name);

    /**
     * Find all products by category.
     *
     * @param category the product category
     * @return a list of products in the specified category
     */
    List<Product> findByCategory(String category);

    /**
     * Find all products by SKU.
     *
     * @param sku the product SKU
     * @return an Optional containing the product if found
     */
    Optional<Product> findBySku(String sku);

    /**
     * Find all products with price less than or equal to the specified amount.
     *
     * @param maxPrice the maximum price
     * @return a list of products within the price range
     */
    List<Product> findByPriceLessThanEqual(Double maxPrice);

    /**
     * Find all products with price greater than or equal to the specified amount.
     *
     * @param minPrice the minimum price
     * @return a list of products within the price range
     */
    List<Product> findByPriceGreaterThanEqual(Double minPrice);

    /**
     * Find all products within a price range.
     *
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return a list of products within the specified price range
     */
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    /**
     * Find all products with stock greater than the specified quantity.
     *
     * @param quantity the minimum stock quantity
     * @return a list of in-stock products
     */
    List<Product> findByStockGreaterThan(Integer quantity);

    /**
     * Find all products that are currently in stock.
     *
     * @return a list of products with stock greater than 0
     */
    @Query("SELECT p FROM Product p WHERE p.stock > 0")
    List<Product> findAllInStock();

    /**
     * Find all products that are out of stock.
     *
     * @return a list of products with zero stock
     */
    @Query("SELECT p FROM Product p WHERE p.stock = 0")
    List<Product> findAllOutOfStock();

    /**
     * Find all products by category with minimum price filter.
     *
     * @param category the product category
     * @param minPrice the minimum price
     * @return a list of products matching the criteria
     */
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.price >= :minPrice")
    List<Product> findByCategoryAndMinPrice(@Param("category") String category, @Param("minPrice") Double minPrice);

    /**
     * Find all products by category within a price range.
     *
     * @param category the product category
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return a list of products matching the criteria
     */
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByCategoryAndPriceRange(@Param("category") String category, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    /**
     * Search products by name containing the specified keyword.
     *
     * @param keyword the search keyword
     * @return a list of products matching the search criteria
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByKeyword(@Param("keyword") String keyword);

    /**
     * Search products by name or description containing the specified keyword.
     *
     * @param keyword the search keyword
     * @return a list of products matching the search criteria
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByKeywordInNameOrDescription(@Param("keyword") String keyword);

    /**
     * Find all products sorted by price in ascending order.
     *
     * @return a list of products sorted by price
     */
    @Query("SELECT p FROM Product p ORDER BY p.price ASC")
    List<Product> findAllSortedByPriceAsc();

    /**
     * Find all products sorted by price in descending order.
     *
     * @return a list of products sorted by price
     */
    @Query("SELECT p FROM Product p ORDER BY p.price DESC")
    List<Product> findAllSortedByPriceDesc();

    /**
     * Count the total number of products in a specific category.
     *
     * @param category the product category
     * @return the count of products in the category
     */
    Long countByCategory(String category);

    /**
     * Check if a product with the specified name exists.
     *
     * @param name the product name
     * @return true if the product exists, false otherwise
     */
    Boolean existsByName(String name);

    /**
     * Check if a product with the specified SKU exists.
     *
     * @param sku the product SKU
     * @return true if the product exists, false otherwise
     */
    Boolean existsBySku(String sku);

    /**
     * Find all products by category ID.
     *
     * @param categoryId the category ID
     * @return a list of products in the specified category
     */
    List<Product> findByCategoryId(Long categoryId);

    /**
     * Find products by name or description containing keywords (case insensitive).
     *
     * @param nameKeyword the keyword to search in name
     * @param descriptionKeyword the keyword to search in description
     * @return a list of products matching the search criteria
     */
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String nameKeyword, String descriptionKeyword);
}
