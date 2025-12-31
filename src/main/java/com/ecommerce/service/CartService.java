package com.ecommerce.service;

import com.ecommerce.dto.CartDTO;

/**
 * Cart Service Interface
 */
public interface CartService {

    CartDTO getCartByUserId(Long userId);

    CartDTO addItemToCart(Long userId, Long productId, Integer quantity);

    CartDTO updateCartItem(Long userId, Long productId, Integer quantity);

    CartDTO removeItemFromCart(Long userId, Long productId);

    void clearCart(Long userId);

    CartDTO getCurrentUserCart();
}
