package ru.javaweb.newsLoader.utils;

import com.google.gson.annotations.SerializedName;

public class ContentOfApiArticle {
    public ContentOfApiArticle() {
    }

    public ContentOfApiArticle(String id, String title, String newsSite, String url, String published_date,
                               String article_text) {
        this.id = id;
        this.title = title;
        this.newsSite = newsSite;
        this.url = url;
        this.published_date = published_date;
        this.article_text = article_text;
    }

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("newsSite")
    private String newsSite;

    @SerializedName("url")
    private String url;

    @SerializedName("publishedAt")
    private String published_date;

    @SerializedName("summary")
    private String article_text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public String getArticle_text() {
        return article_text;
    }

    public void setArticle_text(String article_text) {
        this.article_text = article_text;
    }

    public String getNewsSite() {
        return newsSite;
    }

    public void setNewsSite(String newsSite) {
        this.newsSite = newsSite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ContentOfApiArticle{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", newsSite='" + newsSite + '\'' +
                ", url='" + url + '\'' +
                ", published_date='" + published_date + '\'' +
                ", article_text='" + article_text + '\'' +
                '}';
    }
}
