package com.example.wydad.web.rest;

import com.example.wydad.entities.Game;
import com.example.wydad.entities.Ticket;
import com.example.wydad.services.GameService;
import com.example.wydad.services.TicketService;
import com.example.wydad.web.DTOs.GameDTO;
import com.example.wydad.web.DTOs.TicketDTO;
import com.example.wydad.web.DTOs.mapper.TicketMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final GameService gameService;
    private final TicketMapper ticketMapper;

    public TicketController(TicketService ticketService, GameService gameService, TicketMapper ticketMapper) {
        this.ticketService = ticketService;
        this.gameService = gameService;
        this.ticketMapper = ticketMapper;
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        List<TicketDTO> ticketDTOs = tickets.stream()
                .map(ticketMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(ticketDTOs, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Integer id) {
        return ticketService.getTicketById(id)
                .map(ticketMapper::toDTO) // Map Ticket to TicketDTO
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody Ticket ticket) {
        if (ticket.getGame() == null || ticket.getGame().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Game game = gameService.getGameById(ticket.getGame().getId())
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + ticket.getGame().getId()));
        ticket.setGame(game);

        Ticket newTicket = ticketService.saveTicket(ticket);
        return new ResponseEntity<>(ticketMapper.toDTO(newTicket), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Integer id) {
        ticketService.deleteTicket(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
