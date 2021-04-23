package ru.javaweb.newsLoader.repository;

import ru.javaweb.newsLoader.model.Article;

import java.util.List;

public interface ArticleRepository {

    Article save(Article article);

    boolean delete(int id);

    Article get(int id);

    List<Article> getAll();

    List<Article> getOnNewsSite(String siteName);
}
