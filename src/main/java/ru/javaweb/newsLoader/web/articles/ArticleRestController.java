package ru.javaweb.newsLoader.web.articles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javaweb.newsLoader.model.Article;
import ru.javaweb.newsLoader.service.ArticleService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = ArticleRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ArticleRestController {
    static final String REST_URL = "/article_api";

    @Autowired
    private ArticleService service;

    //Get article with specified in id
    @GetMapping("/{id}")
    public Article get(@PathVariable("id") int id) {
            return service.get(id);
    }

    //Get article with specified site name
    @GetMapping("/newssite/{siteName}")
    public List<Article> getBySiteName(@PathVariable("siteName") String siteName) {
        return service.getOnNewsSite(siteName);
    }

    //Get all time_entries from the data base
    @GetMapping("/all")
    public List<Article> getAll() {
        return service.getAll();
    }
}
