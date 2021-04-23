package ru.javaweb.newsLoader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaweb.newsLoader.model.Article;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudArticleRepository extends JpaRepository<Article, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Article t WHERE t.articleId=:id")
    int delete(@Param("id") int id);

    @Override
    Article save(Article item);

    @Query("SELECT t FROM Article t")
    List<Article> getAll();

    @Query("SELECT t FROM Article t WHERE t.newsSite=:siteName")
    List<Article> findBySiteName(@Param("siteName") String siteName);

    @Query("SELECT t FROM Article t WHERE t.title=:title")
    List<Article> getByTitle(@Param("title") String title);
}
