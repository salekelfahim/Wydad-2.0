package com.example.wydad.web.DTOs;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CartDTO {
    private Integer id;
    private Integer userId;
    private List<CartItemDTO> items;
    private Double total;
}
