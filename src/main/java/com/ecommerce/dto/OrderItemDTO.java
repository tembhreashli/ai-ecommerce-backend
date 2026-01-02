package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Order Item DTO for API requests and responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {

    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
