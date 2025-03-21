package com.example.wydad.repositories;

import com.example.wydad.entities.Cart;
import com.example.wydad.entities.CartItem;
import com.example.wydad.entities.Product;
import com.example.wydad.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    Optional<CartItem> findByCartAndTicket(Cart cart, Ticket ticket);
}