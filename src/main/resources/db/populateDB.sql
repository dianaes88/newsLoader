--Delete existing rows for testing purpose only

DELETE FROM ARTICLES WHERE id > 0;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO ARTICLES(id, title, news_site, published_date, article) VALUES
             (1, '1','1','2015-05-30', 'asdf'),
             (2, '2','2','2015-06-01', 'rts'),
             (3, '3','3','2015-07-02', 'hhh');
