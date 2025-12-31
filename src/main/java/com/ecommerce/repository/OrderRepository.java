package com.ecommerce.repository;

import com.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Order entity.
 * Provides CRUD operations and custom queries for Order management.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find all orders by customer ID
     *
     * @param customerId the customer ID
     * @return list of orders for the customer
     */
    List<Order> findByCustomerId(Long customerId);

    /**
     * Find all orders by user ID (alias for customer ID)
     *
     * @param userId the user ID
     * @return list of orders for the user
     */
    List<Order> findByUserId(Long userId);

    /**
     * Find an order by order number
     *
     * @param orderNumber the order number
     * @return optional containing the order if found
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * Find orders by status
     *
     * @param status the order status
     * @return list of orders with the given status
     */
    List<Order> findByStatus(String status);

    /**
     * Find orders by user ID and status
     *
     * @param userId the user ID
     * @param status the order status
     * @return list of orders matching both criteria
     */
    List<Order> findByUserIdAndStatus(Long userId, String status);

    /**
     * Find orders created within a date range
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return list of orders created between the dates
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findOrdersByDateRange(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    /**
     * Find recent orders by user ID
     *
     * @param userId the user ID
     * @param limit the number of orders to retrieve
     * @return list of recent orders
     */
    @Query(value = "SELECT * FROM orders WHERE user_id = :userId ORDER BY created_at DESC LIMIT :limit",
           nativeQuery = true)
    List<Order> findRecentOrdersByUserId(@Param("userId") Long userId, @Param("limit") int limit);

    /**
     * Count orders by status
     *
     * @param status the order status
     * @return count of orders with the given status
     */
    long countByStatus(String status);

    /**
     * Check if an order exists for a user
     *
     * @param userId the user ID
     * @param orderNumber the order number
     * @return true if the order exists, false otherwise
     */
    boolean existsByUserIdAndOrderNumber(Long userId, String orderNumber);
}
