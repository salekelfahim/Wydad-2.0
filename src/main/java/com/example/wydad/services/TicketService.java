package com.example.wydad.services;

import com.example.wydad.entities.Ticket;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    List<Ticket> getAllTickets();
    Optional<Ticket> getTicketById(Integer id);
    Ticket saveTicket(Ticket ticket);
    void deleteTicket(Integer id);
}