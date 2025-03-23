package com.example.wydad.web.rest;

import com.example.wydad.entities.Player;
import com.example.wydad.services.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Integer id) {
        Player player = playerService.getPlayerById(id).orElse(null);
        if (player != null) {
            return new ResponseEntity<>(player, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Player> create(@RequestBody Player player) {
        Player newPlayer = playerService.save(player);
        return new ResponseEntity<>(newPlayer, HttpStatus.CREATED);
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Player> createPlayer(
            @RequestPart("player") Player player,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            Player newPlayer = playerService.savePlayer(player, image);
            return new ResponseEntity<>(newPlayer, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Player> updatePlayer(
            @PathVariable Integer id,
            @RequestPart("player") Player player,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            player.setId(id);
            Player updatedPlayer = playerService.savePlayer(player, image);
            return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Integer id) {
        playerService.deletePlayer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}