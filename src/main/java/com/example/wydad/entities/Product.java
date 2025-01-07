package com.example.wydad.entities;

import com.example.wydad.entities.enums.Size;
import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String type;
    private Integer quantity;
    private Float price;

    @Enumerated(EnumType.STRING)
    private Size size;
}
