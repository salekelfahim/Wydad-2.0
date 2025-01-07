package com.example.wydad.services.impl;

import com.example.wydad.entities.Ticket;
import com.example.wydad.repositories.TicketRepository;
import com.example.wydad.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Optional<Ticket> getTicketById(Integer id) {
        return ticketRepository.findById(id);
    }

    @Override
    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicket(Integer id) {
        ticketRepository.deleteById(id);
    }
}