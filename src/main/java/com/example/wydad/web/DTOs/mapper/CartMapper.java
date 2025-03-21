package com.example.wydad.web.DTOs.mapper;

import com.example.wydad.entities.Cart;
import com.example.wydad.entities.CartItem;
import com.example.wydad.web.DTOs.CartDTO;
import com.example.wydad.web.DTOs.CartItemDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartDTO toDTO(Cart cart) {
        if (cart == null) {
            return null;
        }

        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(this::toCartItemDTO)
                .collect(Collectors.toList());

        double total = itemDTOs.stream().mapToDouble(CartItemDTO::getSubtotal).sum();

        return CartDTO.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(itemDTOs)
                .total(total)
                .build();
    }

    public CartItemDTO toCartItemDTO(CartItem item) {
        if (item == null) {
            return null;
        }

        CartItemDTO.CartItemDTOBuilder builder = CartItemDTO.builder()
                .id(item.getId())
                .quantity(item.getQuantity());

        if (item.getProduct() != null) {
            builder.productId(item.getProduct().getId())
                    .name(item.getProduct().getName())
                    .price(item.getProduct().getPrice())
                    .type("product")
                    .picture(item.getProduct().getPicture())
                    .subtotal(item.getProduct().getPrice() * item.getQuantity());
        } else if (item.getTicket() != null) {
            builder.ticketId(item.getTicket().getId())
                    .name("Ticket: " + item.getTicket().getCategory())
                    .price(item.getTicket().getPrice())
                    .type("ticket")
                    .subtotal(item.getTicket().getPrice() * item.getQuantity());
        }

        return builder.build();
    }
}