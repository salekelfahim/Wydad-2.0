package com.example.wydad.services.impl;

import com.example.wydad.entities.Player;
import com.example.wydad.repositories.PlayerRepository;
import com.example.wydad.services.PlayerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Value("${file.upload.directory:uploads/images}")
    private String uploadDirectory;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Optional<Player> getPlayerById(Integer id) {
        return playerRepository.findById(id);
    }

    @Override
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Player savePlayerWithImage(Player player, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalFilename = image.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            Path filePath = Paths.get(uploadDirectory, newFilename);
            Files.write(filePath, image.getBytes());

            player.setPicture("/images/" + newFilename);
        }

        return playerRepository.save(player);
    }

    @Override
    public void deletePlayer(Integer id) {
        Optional<Player> player = playerRepository.findById(id);

        if (player.isPresent() && player.get().getPicture() != null) {
            String imagePath = player.get().getPicture();
            if (imagePath.startsWith("/images/")) {
                String filename = imagePath.substring("/images/".length());
                Path filePath = Paths.get(uploadDirectory, filename);
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    System.err.println("Failed to delete image file: " + e.getMessage());
                }
            }
        }

        playerRepository.deleteById(id);
    }
}