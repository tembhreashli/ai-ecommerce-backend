package com.ecommerce.repository;

import com.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CartItem entity.
 * Provides CRUD operations and custom query methods for cart item management.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Find all cart items by cart ID.
     *
     * @param cartId the cart ID
     * @return a list of cart items
     */
    List<CartItem> findByCartId(Long cartId);

    /**
     * Find a cart item by cart ID and product ID.
     *
     * @param cartId the cart ID
     * @param productId the product ID
     * @return an Optional containing the cart item if found
     */
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    /**
     * Delete all cart items by cart ID.
     *
     * @param cartId the cart ID
     */
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId")
    void deleteByCartId(@Param("cartId") Long cartId);

    /**
     * Check if a cart item exists for a specific cart and product.
     *
     * @param cartId the cart ID
     * @param productId the product ID
     * @return true if the cart item exists, false otherwise
     */
    boolean existsByCartIdAndProductId(Long cartId, Long productId);

    /**
     * Count cart items by cart ID.
     *
     * @param cartId the cart ID
     * @return the count of cart items
     */
    Long countByCartId(Long cartId);
}
