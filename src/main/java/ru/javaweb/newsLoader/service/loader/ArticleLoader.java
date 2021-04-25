package ru.javaweb.newsLoader.service.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ru.javaweb.newsLoader.model.Article;
import ru.javaweb.newsLoader.service.ArticleService;
import ru.javaweb.newsLoader.utils.ContentOfApiArticle;
import ru.javaweb.newsLoader.utils.DownloadUtils;

import java.io.IOException;
import java.net.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

@Component
@Configuration
@PropertySource(value = "classpath:config.properties")// for getting properties of Environment
public class ArticleLoader {
    @Autowired
    private Environment env;

    @Autowired
    private ArticleService articleService;

    final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.000z");
    private static final Logger log = Logger.getLogger(Runnable.class.getName());
    private static boolean fFirstTime = true;

    private static Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.unn.ru", 8080));


    @EventListener(ContextRefreshedEvent.class)
    public void doItAfterStartup() throws IOException, InterruptedException, ExecutionException {
        Integer threadsNumber          = Integer.parseInt(env.getProperty("app.threadsNumber"));
        Integer totalArticleNumber     = Integer.parseInt(env.getProperty("app.totalArticleNumber"));
        Integer articleNumberPerThread = Integer.parseInt(env.getProperty("app.articleNumberPerThread"));
        Integer maxPublisherRecording  = Integer.parseInt(env.getProperty("app.maxPublisherRecording"));
        String apiUrlTemplate          =                  env.getProperty("app.apiUrlTemplate");
        String blackListString         =                  env.getProperty("app.blackList");
        List<String> blackList = Arrays.asList(blackListString.toLowerCase().split(","));

        if (fFirstTime) {
            ExecutorService service = Executors.newFixedThreadPool(threadsNumber);
            ConcurrentMap<String, Set<ContentOfApiArticle>> mapSiteSet = new ConcurrentHashMap<>();

            int limit = 0;
            for (int i = 0; i < totalArticleNumber; i = i + articleNumberPerThread - 1) {
                limit = (limit <= totalArticleNumber - i) ? articleNumberPerThread: totalArticleNumber - i;

                String apiUrs = String.format(apiUrlTemplate, limit, i);

                service.submit(() -> {
                    String threadName = Thread.currentThread().getName();
                    log.info(threadName + ": Begin downloading '" + apiUrs + "'");

                    LocalDate localdate;
                    Date date;
                    try {
                        // test of list forming
                        ContentOfApiArticle[] listArticle = DownloadUtils.getJsonAsStringFromRequest(apiUrs);
                        // test of writing in the database
                        for (ContentOfApiArticle article : listArticle) {
                            System.out.println(article.toString());
                            localdate = (ZonedDateTime.parse(article.getPublished_date(), format)).toLocalDate();
                            System.out.println("Date:" + localdate.toString());
                            date = Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            Article repArticle = new Article(article.getTitle(), article.getNewsSite(), date,
                                    article.getArticle_text());
                            String newsSite = article.getNewsSite();

                            // check for the blacklist
                            String title = article.getTitle();
                            if (blackList.stream().anyMatch(title.toLowerCase()::contains))
                                continue;

                            // check for the contains newsSite in the mapSiteSet
                            if (mapSiteSet.keySet().contains(newsSite)) {
                                mapSiteSet.get(newsSite).add(article);
                            } else {
                                Set<ContentOfApiArticle> setA = new TreeSet<>(new Comparator<ContentOfApiArticle>() {
                                    @Override
                                    public int compare(ContentOfApiArticle c1, ContentOfApiArticle c2) {
                                        return c1.getPublished_date().compareTo(c2.getPublished_date());
                                    }
                                });
                                setA.add(article);
                                mapSiteSet.put(newsSite, setA);
                            }
                            // check fot the refilling of the records
                            if (mapSiteSet.get(newsSite).size() >= maxPublisherRecording) {
                                for (ContentOfApiArticle c : mapSiteSet.get(newsSite)) {
                                    // persistence to db
                                    saveArticle(c);
                                }
                                mapSiteSet.get(newsSite).removeAll(mapSiteSet.get(newsSite));
                            }
                        }
                        log.info("Thread name:" + threadName + ". Map count : " + mapSiteSet.size());
                    } catch (MalformedURLException e) {
                        log.severe(threadName + ": Malformed url '" + apiUrs + "': " + e.getMessage());
                    } catch (IOException e) {
                        log.severe(threadName + ": Could not open url '" + apiUrs + "': " + e.getMessage());
                    } finally {
                    }

                    log.info(threadName + ": End downloading '" + apiUrs + "'");
                });
            }
            // shutting down service
            service.shutdown();
            // wait terminated for the concMap fulling
            int watingTime = 0;
            int interval = 2;
            while (!service.isTerminated()) {
                TimeUnit.SECONDS.sleep(interval);
                watingTime += interval;
                log.info("Waiting for the thread pool completing. Waiting time is " + watingTime + " seconds.");
            }

            for (String s : mapSiteSet.keySet()) {
                for (ContentOfApiArticle c : mapSiteSet.get(s)) {
                    // persistence to db
                    saveArticle(c);
                }
                mapSiteSet.get(s).removeAll(mapSiteSet.get(s));
            }

            for (Map.Entry<String, Set<ContentOfApiArticle>> entry : mapSiteSet.entrySet()) {
                System.out.println("News Site: " + entry.getKey() + ", Set size: " + entry.getValue().size());
            }
        }
        fFirstTime = false;
    }

    private void saveArticle(ContentOfApiArticle c) {
        String textArticle = "Has not been loaded.";
        try {
            textArticle = DownloadUtils.getTextOnUrl(c.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }

        LocalDate localdate = (ZonedDateTime.parse(c.getPublished_date(),format)).toLocalDate();
        Date date = Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Article repArticle = new Article(c.getTitle(), c.getNewsSite(), date, textArticle);
        articleService.create(repArticle);
    }
}
