package com.example.wydad.repositories;

import com.example.wydad.entities.Game;
import com.example.wydad.entities.Ticket;
import com.example.wydad.entities.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    boolean existsByCategoryAndGame(Category category, Game game);
}