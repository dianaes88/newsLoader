package ru.javaweb.newsLoader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javaweb.newsLoader.model.Article;
import ru.javaweb.newsLoader.repository.ArticleRepository;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository repository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Article get(int id) {
        return repository.get(id);
    }

    @Override
    public void delete(List<Article> entriesToDelete) {
        for(int i = 0; i < entriesToDelete.size(); i++) {
            int id = entriesToDelete.get(i).getArticleId();
            repository.delete(id);
        }
    }

    @Override
    public Article update(Article article) {
        return repository.save(article);
    }

    @Override
    public Article create(Article article) {
        return repository.save(article);
    }

    @Override
    public List<Article> getAll() {
        return repository.getAll();
    }

    @Override
    public void updateStatus(int id, String status) {
        Article arti = this.get(id);
        repository.save(arti);
    }

    @Override
    public  List<Article> getOnNewsSite(String siteName)
    {
        return repository.getOnNewsSite(siteName);
    }

}
