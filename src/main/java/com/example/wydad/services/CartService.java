package com.example.wydad.services;

import com.example.wydad.entities.Cart;
import com.example.wydad.entities.CartItem;
import com.example.wydad.entities.User;
import com.example.wydad.web.DTOs.CartDTO;
import com.example.wydad.web.VMs.AddToCartRequest;
import com.example.wydad.web.VMs.UpdateCartItemRequest;

public interface CartService {

    Cart getOrCreateCart(User user);
    CartItem addProductToCart(Integer userId, Integer productId, Double quantity);
    CartItem addTicketToCart(Integer userId, Integer ticketId, Double quantity);
    void removeItemFromCart(Integer userId, Integer cartItemId);
    void updateCartItemQuantity(Integer userId, Integer cartItemId, Double quantity);
    void clearCart(Integer userId);
    CartDTO getCartDetails(Integer userId);
    Cart checkout(Integer userId);
    CartItem addToCart(Integer userId, AddToCartRequest request);
}