package com.ecommerce.service;

import com.ecommerce.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Order Service Interface
 */
public interface OrderService {

    OrderDTO createOrder(Long userId, String shippingAddress, String billingAddress);

    OrderDTO getOrderById(Long orderId);

    OrderDTO getOrderByOrderNumber(String orderNumber);

    List<OrderDTO> getOrdersByUserId(Long userId);

    Page<OrderDTO> getOrders(Pageable pageable);

    OrderDTO updateOrderStatus(Long orderId, String status);

    OrderDTO updatePaymentStatus(Long orderId, String paymentStatus);

    void cancelOrder(Long orderId);

    List<OrderDTO> getCurrentUserOrders();
}
