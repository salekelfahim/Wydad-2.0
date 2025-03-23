package com.example.wydad.web.rest;

import com.example.wydad.entities.CartItem;
import com.example.wydad.services.CartService;
import com.example.wydad.web.DTOs.CartDTO;
import com.example.wydad.web.VMs.AddToCartRequest;
import com.example.wydad.web.VMs.UpdateCartItemRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Integer userId) {
        CartDTO cartDTO = cartService.getCartDetails(userId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartDTO> addToCart(
            @PathVariable Integer userId,
            @RequestBody AddToCartRequest request) {
        cartService.addToCart(userId, request);
        CartDTO updatedCart = cartService.getCartDetails(userId);
        return new ResponseEntity<>(updatedCart, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/items/{itemId}")
    public ResponseEntity<CartDTO> updateCartItem(
            @PathVariable Integer userId,
            @PathVariable Integer itemId,
            @RequestBody UpdateCartItemRequest request) {
        cartService.updateCartItemQuantity(userId, itemId, request.getQuantity());
        CartDTO updatedCart = cartService.getCartDetails(userId);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<CartDTO> removeFromCart(
            @PathVariable Integer userId,
            @PathVariable Integer itemId) {
        cartService.removeItemFromCart(userId, itemId);
        CartDTO updatedCart = cartService.getCartDetails(userId);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Integer userId) {
        cartService.clearCart(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<String> checkout(@PathVariable Integer userId) {
        String message = cartService.checkout(userId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}