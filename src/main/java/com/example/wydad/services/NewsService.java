package com.example.wydad.services;

import com.example.wydad.entities.News;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface NewsService {
    List<News> getAllNews();
    Optional<News> getNewsById(Integer id);
    News saveNews(News news, MultipartFile coverImage) throws IOException;
    News updateNews(Integer id, News news, MultipartFile coverImage) throws IOException;
    void deleteNews(Integer id);
}