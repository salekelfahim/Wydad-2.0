package com.example.wydad.services.impl;

import com.example.wydad.entities.News;
import com.example.wydad.repositories.NewsRepository;
import com.example.wydad.services.NewsService;
import com.example.wydad.web.exceptions.InvalidImageException;
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
public class NewsServiceImpl implements NewsService {

    @Value("${file.upload.directory:uploads/images}")
    private String uploadDirectory;
    private final NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public Optional<News> getNewsById(Integer id) {
        return newsRepository.findById(id);
    }


    @Override
    public News saveNews(News news, MultipartFile coverImage) throws IOException {
        if (coverImage != null && !coverImage.isEmpty()) {
            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalFilename = coverImage.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new InvalidImageException("Cover image filename is missing");
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            Path filePath = Paths.get(uploadDirectory, newFilename);
            Files.write(filePath, coverImage.getBytes());

            news.setCover("/images/" + newFilename);
        } else {
            throw new InvalidImageException("Cover image file is required");
        }
        return newsRepository.save(news);
    }

    @Override
    public News updateNews(Integer id, News news, MultipartFile coverImage) throws IOException {
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));

        existingNews.setTitle(news.getTitle());
        existingNews.setContent(news.getContent());

        if (coverImage != null && !coverImage.isEmpty()) {
            if (existingNews.getCover() != null) {
                String oldImagePath = existingNews.getCover().replace("/images/", "");
                Path oldFilePath = Paths.get(uploadDirectory, oldImagePath);
                Files.deleteIfExists(oldFilePath);
            }

            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalFilename = coverImage.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new InvalidImageException("Cover image filename is missing");
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            Path filePath = Paths.get(uploadDirectory, newFilename);
            Files.write(filePath, coverImage.getBytes());

            existingNews.setCover("/images/" + newFilename);
        }

        return newsRepository.save(existingNews);
    }

    @Override
    public void deleteNews(Integer id) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent() && news.get().getCover() != null) {
            String imagePath = news.get().getCover();
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
        newsRepository.deleteById(id);
    }
}