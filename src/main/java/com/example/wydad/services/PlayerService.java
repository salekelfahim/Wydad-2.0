package com.example.wydad.services;

import com.example.wydad.entities.Player;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PlayerService {
    List<Player> getAllPlayers();
    Optional<Player> getPlayerById(Integer id);
    Player savePlayer(Player player);
    Player savePlayerWithImage(Player player, MultipartFile image) throws IOException;
    void deletePlayer(Integer id);
}