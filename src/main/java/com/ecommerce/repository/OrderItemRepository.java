package com.ecommerce.repository;

import com.ecommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for OrderItem entity.
 * Provides CRUD operations and custom query methods for order item management.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Find all order items by order ID.
     *
     * @param orderId the order ID
     * @return a list of order items
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * Find all order items by product ID.
     *
     * @param productId the product ID
     * @return a list of order items
     */
    List<OrderItem> findByProductId(Long productId);

    /**
     * Count order items by order ID.
     *
     * @param orderId the order ID
     * @return the count of order items
     */
    Long countByOrderId(Long orderId);

    /**
     * Find order items by order ID and product ID.
     *
     * @param orderId the order ID
     * @param productId the product ID
     * @return a list of order items matching the criteria
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.product.id = :productId")
    List<OrderItem> findByOrderIdAndProductId(@Param("orderId") Long orderId, @Param("productId") Long productId);
}
