package com.ecommerce.repository;

import com.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Category entity.
 * Provides CRUD operations and custom query methods for category management.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find a category by its name.
     *
     * @param name the category name
     * @return an Optional containing the category if found
     */
    Optional<Category> findByName(String name);

    /**
     * Find all active categories.
     *
     * @param isActive the active status
     * @return a list of active categories
     */
    List<Category> findByIsActive(Boolean isActive);

    /**
     * Check if a category with the specified name exists.
     *
     * @param name the category name
     * @return true if the category exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find categories by name containing the specified keyword (case insensitive).
     *
     * @param keyword the search keyword
     * @return a list of categories matching the search criteria
     */
    List<Category> findByNameContainingIgnoreCase(String keyword);
}
