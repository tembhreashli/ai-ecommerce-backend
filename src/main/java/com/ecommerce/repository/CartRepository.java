package com.ecommerce.repository;

import com.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Cart entity.
 * Provides CRUD operations and custom query methods for Cart management.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Find a cart by user ID.
     *
     * @param userId the user ID
     * @return an Optional containing the cart if found, otherwise empty
     */
    Optional<Cart> findByUserId(Long userId);

    /**
     * Check if a cart exists for a specific user.
     *
     * @param userId the user ID
     * @return true if a cart exists for the user, false otherwise
     */
    boolean existsByUserId(Long userId);

    /**
     * Delete a cart by user ID.
     *
     * @param userId the user ID
     */
    void deleteByUserId(Long userId);
}
