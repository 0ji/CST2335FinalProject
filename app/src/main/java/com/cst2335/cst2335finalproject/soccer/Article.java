package com.cst2335.cst2335finalproject.soccer;

public class Article {
    private long id;
    private String title, link, description, pubDate;
    public Article(String title, String link, String description, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.id = 1;
    }
    public String getTitle(){return this.title;}
    public String getLink(){return this.link;}
    public String getDescription(){return this.description;}
    public String getPubDate(){return this.pubDate;}
    public String toString(){
        return title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
