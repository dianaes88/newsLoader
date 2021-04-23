package ru.javaweb.newsLoader.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaweb.newsLoader.model.Article;

import java.util.List;

@Repository
public class DataJpaArticleRepositoryImpl implements ArticleRepository {
    @Autowired
    CrudArticleRepository crudArticleRepository;

    @Override
    @Transactional
    public Article save(Article article) {
        List<Article> a = crudArticleRepository.getByTitle(article.getTitle());
        if (a != null && !a.isEmpty())
        {
            return a.get(0);
        }
        return crudArticleRepository.save(article);
    }

    @Modifying
    @Transactional
    @Override
    public boolean delete(int id) {
        return crudArticleRepository.delete(id) != 0;
    }

    @Override
    public Article get(int id) {
        return crudArticleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Article> getAll() {
        return crudArticleRepository.findAll();
    }

    @Override
    public List<Article> getOnNewsSite(String siteName)
    {
        return crudArticleRepository.findBySiteName(siteName);
    }
}
