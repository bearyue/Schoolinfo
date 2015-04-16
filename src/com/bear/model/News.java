package com.bear.model;

/**
 * Created by bear on 2015/4/6.
 */
public class News {
    int news_id;
    String news_content;
    String news_time;
    String news_address;
    String news_img;
    int news_type;

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getNews_content() {
        return news_content;
    }

    public void setNews_content(String news_content) {
        this.news_content = news_content;
    }

    public String getNews_time() {
        return news_time;
    }

    public void setNews_time(String news_time) {
        this.news_time = news_time;
    }

    public String getNews_img() {
        return news_img;
    }

    public void setNews_img(String news_img) {
        this.news_img = news_img;
    }

    public String getNews_address() {
        return news_address;
    }

    public void setNews_address(String news_address) {
        this.news_address = news_address;
    }

    public int getNews_type() {
        return news_type;
    }

    public void setNews_type(int news_type) {
        this.news_type = news_type;
    }
}
