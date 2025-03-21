package com.example.wydad.services.impl;

import com.example.wydad.entities.*;
import com.example.wydad.repositories.CartItemRepository;
import com.example.wydad.repositories.CartRepository;
import com.example.wydad.repositories.ProductRepository;
import com.example.wydad.repositories.TicketRepository;
import com.example.wydad.services.CartService;
import com.example.wydad.services.SaleService;
import com.example.wydad.services.UserService;
import com.example.wydad.web.DTOs.CartDTO;
import com.example.wydad.web.DTOs.mapper.CartMapper;
import com.example.wydad.web.VMs.AddToCartRequest;
import com.example.wydad.web.exceptions.InsufficientStockException;
import com.example.wydad.web.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final TicketRepository ticketRepository;
    private final SaleService saleService;
    private final CartMapper cartMapper;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
                           UserService userService, ProductRepository productRepository,
                           TicketRepository ticketRepository, SaleService saleService,
                           CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.productRepository = productRepository;
        this.ticketRepository = ticketRepository;
        this.saleService = saleService;
        this.cartMapper = cartMapper;
    }

    @Override
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .items(new ArrayList<>())
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    @Override
    @Transactional
    public CartItem addToCart(Integer userId, AddToCartRequest request) {
        if (request.getProductId() != null) {
            return addProductToCart(userId, request.getProductId(), request.getQuantity());
        } else if (request.getTicketId() != null) {
            return addTicketToCart(userId, request.getTicketId(), request.getQuantity());
        } else {
            throw new IllegalArgumentException("Either productId or ticketId must be provided");
        }
    }

    @Override
    @Transactional
    public CartItem addProductToCart(Integer userId, Integer productId, Double quantity) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException("Not enough product stock available. Requested: " +
                    quantity + ", Available: " + product.getQuantity());
        }

        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();

            cart.getItems().add(newItem);
            cartRepository.save(cart);
            return newItem;
        }
    }

    @Override
    @Transactional
    public CartItem addTicketToCart(Integer userId, Integer ticketId, Double quantity) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        if (ticket.getQuantity() < quantity) {
            throw new InsufficientStockException("Not enough tickets available. Requested: " +
                    quantity + ", Available: " + ticket.getQuantity());
        }

        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndTicket(cart, ticket);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .ticket(ticket)
                    .quantity(quantity)
                    .build();

            cart.getItems().add(newItem);
            cartRepository.save(cart);
            return newItem;
        }
    }

    @Override
    @Transactional
    public void removeItemFromCart(Integer userId, Integer cartItemId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Cart item doesn't belong to the user's cart");
        }

        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void updateCartItemQuantity(Integer userId, Integer cartItemId, Double quantity) {
        if (quantity <= 0) {
            removeItemFromCart(userId, cartItemId);
            return;
        }

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Cart item doesn't belong to the user's cart");
        }

        if (item.getProduct() != null) {
            if (item.getProduct().getQuantity() < quantity) {
                throw new InsufficientStockException("Not enough product stock available. Requested: " +
                        quantity + ", Available: " + item.getProduct().getQuantity());
            }
        } else if (item.getTicket() != null) {
            if (item.getTicket().getQuantity() < quantity) {
                throw new InsufficientStockException("Not enough tickets available. Requested: " +
                        quantity + ", Available: " + item.getTicket().getQuantity());
            }
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);
    }

    @Override
    @Transactional
    public void clearCart(Integer userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDTO getCartDetails(Integer userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> Cart.builder().user(user).items(new ArrayList<>()).build());

        return cartMapper.toDTO(cart);
    }

    @Override
    @Transactional
    public Cart checkout(Integer userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart");
        }

        for (CartItem item : cart.getItems()) {
            Sale sale = new Sale();
            sale.setUser(user);
            sale.setQuantity(item.getQuantity());

            if (item.getProduct() != null) {
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() - item.getQuantity().intValue());
                productRepository.save(product);
                sale.setProduct(product);
            }

            if (item.getTicket() != null) {
                Ticket ticket = item.getTicket();
                ticket.setQuantity(ticket.getQuantity() - item.getQuantity().intValue());
                ticketRepository.save(ticket);
                sale.setTicket(ticket);
            }

            saleService.saveSale(sale);
        }

        cart.getItems().clear();
        return cartRepository.save(cart);
    }
}