package com.ecommerce.service;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Order;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Order Service Implementation
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository,
                           CartRepository cartRepository,
                           UserService userService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
    }

    @Override
    public OrderDTO createOrder(Long userId, String shippingAddress, String billingAddress) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", userId));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Cannot create order from empty cart");
        }

        String orderNumber = generateOrderNumber();

        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setCustomerId(userId);
        order.setStatus(Order.Status.PENDING);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);
        order.setTotalAmount(cart.getTotalPrice());
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);

        Order savedOrder = orderRepository.save(order);

        // Clear the cart after creating order
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setItemCount(0);
        cartRepository.save(cart);

        return convertToDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        return convertToDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));
        return convertToDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByCustomerId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        Order.Status orderStatus = parseOrderStatus(status);
        order.setStatus(orderStatus);

        if (orderStatus == Order.Status.SHIPPED && order.getShippedAt() == null) {
            order.setShippedAt(LocalDateTime.now());
        } else if (orderStatus == Order.Status.DELIVERED && order.getDeliveredAt() == null) {
            order.setDeliveredAt(LocalDateTime.now());
        }

        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }

    @Override
    public OrderDTO updatePaymentStatus(Long orderId, String paymentStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        Order.PaymentStatus status = parsePaymentStatus(paymentStatus);
        order.setPaymentStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (order.getStatus() == Order.Status.SHIPPED || 
            order.getStatus() == Order.Status.DELIVERED) {
            throw new BadRequestException("Cannot cancel order that has been shipped or delivered");
        }

        order.setStatus(Order.Status.CANCELLED);
        order.setPaymentStatus(Order.PaymentStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getCurrentUserOrders() {
        Long userId = userService.getCurrentUserId();
        return getOrdersByUserId(userId);
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + 
               UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Parse order status string to enum, with proper error handling
     */
    private Order.Status parseOrderStatus(String status) {
        try {
            return Order.Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid order status: " + status + 
                ". Valid values are: PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, RETURNED");
        }
    }

    /**
     * Parse payment status string to enum, with proper error handling
     */
    private Order.PaymentStatus parsePaymentStatus(String status) {
        try {
            return Order.PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid payment status: " + status + 
                ". Valid values are: PENDING, COMPLETED, FAILED, REFUNDED, PARTIALLY_REFUNDED, CANCELLED");
        }
    }

    private OrderDTO convertToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomerId())
                .status(order.getStatus() != null ? order.getStatus().name() : null)
                .paymentStatus(order.getPaymentStatus() != null ? order.getPaymentStatus().name() : null)
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .billingAddress(order.getBillingAddress())
                .notes(order.getNotes())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .shippedAt(order.getShippedAt())
                .deliveredAt(order.getDeliveredAt())
                .build();
    }
}
