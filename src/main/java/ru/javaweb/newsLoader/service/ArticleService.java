package ru.javaweb.newsLoader.service;

import ru.javaweb.newsLoader.model.Article;

import java.util.List;

public interface ArticleService {
    Article get(int id);

    void delete(List<Article> entriesToDelete);

    Article update(Article article);

    Article create(Article article);

    List<Article> getAll();

    void updateStatus(int id, String status);

    List<Article> getOnNewsSite(String siteName);
}
