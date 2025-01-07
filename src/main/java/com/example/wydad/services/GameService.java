package com.example.wydad.services;

import com.example.wydad.entities.Game;
import java.util.List;
import java.util.Optional;

public interface GameService {
    List<Game> getAllGames();
    Optional<Game> getGameById(Integer id);
    Game saveGame(Game game);
    void deleteGame(Integer id);
}