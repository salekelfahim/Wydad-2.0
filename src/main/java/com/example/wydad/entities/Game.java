package com.example.wydad.entities;

import com.example.wydad.entities.enums.Competition;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate date;
    private String time;
    private String opponent;

    @Enumerated(EnumType.STRING)
    private Competition competition;
}
