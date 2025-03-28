package com.example.wydad.services;

import com.example.wydad.entities.Player;
import com.example.wydad.entities.enums.Position;
import com.example.wydad.repositories.PlayerRepository;
import com.example.wydad.services.impl.PlayerServiceImpl;
import com.example.wydad.web.exceptions.InvalidImageException;
import com.example.wydad.web.exceptions.InvalidPlayerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private Player testPlayer;
    private String tempUploadDir;

    @BeforeEach
    void setUp() {
        tempUploadDir = "test-uploads/images";
        ReflectionTestUtils.setField(playerService, "uploadDirectory", tempUploadDir);

        testPlayer = Player.builder()
                .id(1)
                .firstName("Lionel")
                .lastName("Messi")
                .birthday(LocalDate.of(1987, 6, 24))
                .nationality("Argentinian")
                .number(10)
                .position(Position.ATTACKER)
                .build();
    }

    @Test
    void getAllPlayers_ShouldReturnAllPlayers() {
        List<Player> expectedPlayers = Arrays.asList(
                testPlayer,
                Player.builder().id(2).firstName("Cristiano").lastName("Ronaldo").position(Position.ATTACKER).build()
        );
        when(playerRepository.findAll()).thenReturn(expectedPlayers);

        List<Player> actualPlayers = playerService.getAllPlayers();

        assertEquals(expectedPlayers.size(), actualPlayers.size());
        assertEquals(expectedPlayers, actualPlayers);
        verify(playerRepository, times(1)).findAll();
    }

    @Test
    void getPlayerById_ExistingId_ShouldReturnPlayer() {
        when(playerRepository.findById(1)).thenReturn(Optional.of(testPlayer));

        Optional<Player> result = playerService.getPlayerById(1);

        assertTrue(result.isPresent());
        assertEquals(testPlayer, result.get());
        verify(playerRepository, times(1)).findById(1);
    }

    @Test
    void getPlayerById_NonExistingId_ShouldReturnEmptyOptional() {
        when(playerRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Player> result = playerService.getPlayerById(999);

        assertTrue(result.isEmpty());
        verify(playerRepository, times(1)).findById(999);
    }

    @Test
    void savePlayer_ShouldSaveAndReturnPlayer() {
        when(playerRepository.save(any(Player.class))).thenReturn(testPlayer);

        Player savedPlayer = playerService.save(testPlayer);

        assertEquals(testPlayer, savedPlayer);
        verify(playerRepository, times(1)).save(testPlayer);
    }

    @Test
    void deletePlayer_WithImage_ShouldDeletePlayerAndImage() throws IOException {
        String imageName = "test-image.jpg";
        testPlayer.setPicture("/images/" + imageName);

        Files.createDirectories(Paths.get(tempUploadDir));
        Path imagePath = Paths.get(tempUploadDir, imageName);
        Files.write(imagePath, "test content".getBytes());

        when(playerRepository.findById(1)).thenReturn(Optional.of(testPlayer));

        playerService.deletePlayer(1);

        verify(playerRepository).deleteById(1);
        assertFalse(Files.exists(imagePath), "Image file should be deleted");

        Files.deleteIfExists(Paths.get(tempUploadDir));
    }

    @Test
    void savePlayer_WithNullPlayer_ShouldThrowInvalidPlayerException() {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        InvalidPlayerException exception = assertThrows(InvalidPlayerException.class, () -> {
            playerService.savePlayer(null, imageFile);
        });

        assertEquals("Player object cannot be null", exception.getMessage());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void savePlayer_WithExistingId_ShouldThrowInvalidPlayerException() {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        Player playerWithId = Player.builder()
                .id(1)
                .firstName("Lionel")
                .lastName("Messi")
                .build();

        InvalidPlayerException exception = assertThrows(InvalidPlayerException.class, () -> {
            playerService.savePlayer(playerWithId, imageFile);
        });

        assertEquals("Cannot save player with existing ID. Use update method instead.", exception.getMessage());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void savePlayer_WithMissingRequiredFields_ShouldThrowInvalidPlayerException() {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        Player incompletePlayer = Player.builder()
                .firstName("")
                .lastName("Messi")
                .build();

        InvalidPlayerException exception = assertThrows(InvalidPlayerException.class, () -> {
            playerService.savePlayer(incompletePlayer, imageFile);
        });

        assertEquals("Player must have first name and last name", exception.getMessage());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void savePlayer_WithNullImage_ShouldThrowInvalidImageException() {
        Player validPlayer = Player.builder()
                .firstName("Lionel")
                .lastName("Messi")
                .birthday(LocalDate.of(1987, 6, 24))
                .nationality("Argentinian")
                .position(Position.ATTACKER)
                .build();

        InvalidImageException exception = assertThrows(InvalidImageException.class, () -> {
            playerService.savePlayer(validPlayer, null);
        });

        assertEquals("Image file is required", exception.getMessage());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void savePlayer_WithEmptyImage_ShouldThrowInvalidImageException() {
        Player validPlayer = Player.builder()
                .firstName("Lionel")
                .lastName("Messi")
                .birthday(LocalDate.of(1987, 6, 24))
                .nationality("Argentinian")
                .position(Position.ATTACKER)
                .build();

        MockMultipartFile emptyImage = new MockMultipartFile(
                "image",
                "",
                "image/jpeg",
                new byte[0]
        );

        InvalidImageException exception = assertThrows(InvalidImageException.class, () -> {
            playerService.savePlayer(validPlayer, emptyImage);
        });

        assertEquals("Image file is required", exception.getMessage());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void savePlayer_WithValidPlayerAndImage_ShouldSavePlayerWithImagePath() throws IOException {
        Files.createDirectories(Paths.get(tempUploadDir));

        Player validPlayer = Player.builder()
                .firstName("Lionel")
                .lastName("Messi")
                .birthday(LocalDate.of(1987, 6, 24))
                .nationality("Argentinian")
                .position(Position.ATTACKER)
                .build();

        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        when(playerRepository.save(any(Player.class))).thenAnswer(invocation -> {
            Player savedPlayer = invocation.getArgument(0);
            savedPlayer.setId(1);
            return savedPlayer;
        });

        Player result = playerService.savePlayer(validPlayer, imageFile);


        assertNotNull(result);
        assertNotNull(result.getPicture());
        assertTrue(result.getPicture().startsWith("/images/"));

        String filename = result.getPicture().substring("/images/".length());
        Path imagePath = Paths.get(tempUploadDir, filename);
        assertTrue(Files.exists(imagePath));

        Files.deleteIfExists(imagePath);
        Files.deleteIfExists(Paths.get(tempUploadDir));

        ArgumentCaptor<Player> playerCaptor = ArgumentCaptor.forClass(Player.class);
        verify(playerRepository).save(playerCaptor.capture());
        assertEquals(validPlayer.getFirstName(), playerCaptor.getValue().getFirstName());
        assertEquals(validPlayer.getLastName(), playerCaptor.getValue().getLastName());
    }
}