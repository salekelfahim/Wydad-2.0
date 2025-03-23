package com.example.wydad.services.impl;

import com.example.wydad.entities.Game;
import com.example.wydad.repositories.GameRepository;
import com.example.wydad.services.GameService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public Optional<Game> getGameById(Integer id) {
        return gameRepository.findById(id);
    }

    @Override
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public void deleteGame(Integer id) {
        gameRepository.deleteById(id);
    }
}