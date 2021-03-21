package com.cst2335.cst2335finalproject.soccer;

public class Article {
    private long id;
    private String title, link, description, pubDate;
    public Article(String title, String link, String description, String pubDate, int id) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.id = id;
    }
    public Article(){}
    public String getTitle(){return this.title;}
    public String getLink(){return this.link;}
    public String getDescription(){return this.description;}
    public String getPubDate(){return this.pubDate;}
    public String toString(){
        return title;
    }
    public void setTitle(String title){this.title=title;}
    public void setDescription(String description) {
        this.description = description;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public void setDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
