package com.example.wydad.web.DTOs;

import com.example.wydad.entities.enums.Competition;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class GameDTO {
    private Integer id;
    private LocalDate date;
    private String time;
    private String opponent;
    private Competition competition;

}
