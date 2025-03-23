package com.example.wydad.web.DTOs.mapper;

import com.example.wydad.entities.Ticket;
import com.example.wydad.entities.Game;
import com.example.wydad.web.DTOs.GameDTO;
import com.example.wydad.web.DTOs.TicketDTO;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketDTO toDTO(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        return TicketDTO.builder()
                .id(ticket.getId())
                .price(ticket.getPrice())
                .quantity(ticket.getQuantity())
                .category(ticket.getCategory())
                .game(toGameDTO(ticket.getGame()))
                .build();
    }

    public GameDTO toGameDTO(Game game) {
        if (game == null) {
            return null;
        }

        return GameDTO.builder()
                .id(game.getId())
                .date(game.getDate())
                .time(game.getTime())
                .opponent(game.getOpponent())
                .competition(game.getCompetition())
                .build();
    }
}
