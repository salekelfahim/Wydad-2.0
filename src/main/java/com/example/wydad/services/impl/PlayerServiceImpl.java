package com.example.wydad.services.impl;

import com.example.wydad.entities.Player;
import com.example.wydad.repositories.PlayerRepository;
import com.example.wydad.services.PlayerService;
import com.example.wydad.web.exceptions.InvalidImageException;
import com.example.wydad.web.exceptions.InvalidPlayerException;
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
    public Player save(Player player) {
        if (player == null) {
            throw new InvalidPlayerException("Player object cannot be null");
        }

        if (player.getId() != null) {
            throw new InvalidPlayerException("Cannot save player with existing ID. Use update method instead.");
        }

        if (player.getFirstName() == null || player.getFirstName().trim().isEmpty() ||
                player.getLastName() == null || player.getLastName().trim().isEmpty()) {
            throw new InvalidPlayerException("Player must have first name and last name");
        }

        return playerRepository.save(player);
    }

    @Override
    public Player savePlayer(Player player, MultipartFile image) throws IOException {
        if (player == null) {
            throw new InvalidPlayerException("Player object cannot be null");
        }

        if (player.getId() != null) {
            throw new InvalidPlayerException("Cannot save player with existing ID. Use update method instead.");
        }

        if (player.getFirstName() == null || player.getFirstName().trim().isEmpty() ||
                player.getLastName() == null || player.getLastName().trim().isEmpty()) {
            throw new InvalidPlayerException("Player must have first name and last name");
        }

        if (image != null && !image.isEmpty()) {
            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalFilename = image.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new InvalidImageException("Image filename is missing");
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            Path filePath = Paths.get(uploadDirectory, newFilename);
            Files.write(filePath, image.getBytes());

            player.setPicture("/images/" + newFilename);
        } else {
            throw new InvalidImageException("Image file is required");
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