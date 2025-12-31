package com.ecommerce.controller;

import com.ecommerce.dto.CartDTO;
import com.ecommerce.service.CartService;
import com.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Cart Controller
 * Handles shopping cart operations
 */
@RestController
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    /**
     * Get current user's cart
     */
    @GetMapping
    public ResponseEntity<CartDTO> getCurrentUserCart() {
        CartDTO cart = cartService.getCurrentUserCart();
        return ResponseEntity.ok(cart);
    }

    /**
     * Add item to cart
     */
    @PostMapping("/items")
    public ResponseEntity<CartDTO> addItemToCart(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        Long userId = userService.getCurrentUserId();
        CartDTO cart = cartService.addItemToCart(userId, productId, quantity);
        return ResponseEntity.ok(cart);
    }

    /**
     * Update cart item quantity
     */
    @PutMapping("/items/{productId}")
    public ResponseEntity<CartDTO> updateCartItem(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        Long userId = userService.getCurrentUserId();
        CartDTO cart = cartService.updateCartItem(userId, productId, quantity);
        return ResponseEntity.ok(cart);
    }

    /**
     * Remove item from cart
     */
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartDTO> removeItemFromCart(@PathVariable Long productId) {
        Long userId = userService.getCurrentUserId();
        CartDTO cart = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Clear cart
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        Long userId = userService.getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
