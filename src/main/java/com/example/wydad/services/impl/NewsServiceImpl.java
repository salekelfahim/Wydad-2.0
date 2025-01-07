package com.example.wydad.services.impl;

import com.example.wydad.entities.News;
import com.example.wydad.repositories.NewsRepository;
import com.example.wydad.services.NewsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {

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
    public News saveNews(News news) {
        return newsRepository.save(news);
    }

    @Override
    public void deleteNews(Integer id) {
        newsRepository.deleteById(id);
    }
}