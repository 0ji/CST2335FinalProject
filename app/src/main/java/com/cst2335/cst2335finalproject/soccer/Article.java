package com.cst2335.cst2335finalproject.soccer;

import java.io.Serializable;

public class Article implements Serializable {
    private long id;
    private String title, link, description, pubDate;
    private byte[] thumbnailUrl;

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

    public void setThumbnailUrl(byte[] url) {
        this.thumbnailUrl = url;
    }

    public byte[] getThumbnailUrl() {
        return this.thumbnailUrl;
    }
}
