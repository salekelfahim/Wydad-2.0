package com.example.wydad.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double quantity;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Ticket ticket;
}
