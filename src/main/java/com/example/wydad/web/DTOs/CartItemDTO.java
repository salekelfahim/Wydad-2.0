package com.example.wydad.web.DTOs;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemDTO {
    private Integer id;
    private Integer productId;
    private Integer ticketId;
    private String name;
    private Float price;
    private String type;
    private String picture;
    private Double quantity;
    private Double subtotal;
}
