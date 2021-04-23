package ru.javaweb.newsLoader.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Access(AccessType.FIELD)
@Table(name = "ARTICLES")
public class Article {

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @Column(name = "id")
    protected Integer articleId;

    @Column(name = "title")
    private String title;

    @Column(name = "news_site")
    private String newsSite;

    @Column(name = "published_date", nullable = false)
    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date publishedDate;

    @Column(name = "article")
    private String textArticle;

    public Article() {
    }

    public Article(String title, String newsSite, Date publishedDate, String textArticle) {
        this(null, title, newsSite, publishedDate, textArticle);
    }

    public Article(Integer articleId, String title, String newsSite, Date publishedDate, String textArticle) {
        this.articleId = articleId;
        this.title = title;
        this.newsSite = newsSite;
        this.publishedDate = publishedDate;
        this.textArticle = textArticle;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer id) {
        this.articleId = id;
    }

    public String getNewsSite() {
        return newsSite;
    }

    public void setNewsSite(String newsSite) {
        this.newsSite = newsSite;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getTextArticle() {
        return textArticle;
    }

    public void setTextArticle(String textArticle) {
        this.textArticle = textArticle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", title='" + title + '\'' +
                ", newsSite='" + newsSite + '\'' +
                ", publishedDate=" + publishedDate +
                ", textArticle='" + textArticle + '\'' +
                '}';
    }
}
