package com.example.wydad.entities;

import com.example.wydad.entities.enums.Position;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String nationality;
    private Integer number;

    @Enumerated(EnumType.STRING)
    private Position position;
}
