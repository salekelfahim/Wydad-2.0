package com.example.wydad.services;

import com.example.wydad.entities.Player;
import java.util.List;
import java.util.Optional;

public interface PlayerService {
    List<Player> getAllPlayers();
    Optional<Player> getPlayerById(Integer id);
    Player savePlayer(Player player);
    void deletePlayer(Integer id);
}