package com.example.wydad.web.DTOs;

import com.example.wydad.entities.enums.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TicketDTO {
    private Integer id;
    private Float price;
    private Integer quantity;
    private Category category;
    private GameDTO game;

}


