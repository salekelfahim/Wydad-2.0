package com.example.wydad.services;

import com.example.wydad.entities.News;
import java.util.List;
import java.util.Optional;

public interface NewsService {
    List<News> getAllNews();
    Optional<News> getNewsById(Integer id);
    News saveNews(News news);
    void deleteNews(Integer id);
}