package com.cst2335.cst2335finalproject.soccer;

import java.io.Serializable;
/**
 * Article class
 * This class is a container to store data related to soccer articles
 * This class inherits Serializable interface.
 * */
public class Article implements Serializable {

    private long id;
    private String title, link, description, pubDate;
    private byte[] thumbnailUrl;
    /**
     *The default constructor
     * */
    public Article(){}
    /**
     *The constructor which takes parameters
     * @param id is an id
     * @param title is an article's title
     * @param link is an article's link
     * @param pubDate is an article's date
     * @param description is an article's description
     * @param thumbnailUrl is an image data in a byte array format
     * The container sets data as class members
     * */
    public Article(long id, String title, String link, String pubDate, String description, byte[] thumbnailUrl){
        this.id=id;this.title=title;this.link=link;this.description=description;this.pubDate=pubDate;this.thumbnailUrl=thumbnailUrl;
    }
    /**
     *getTitle returns a title of an article instance.
     * @return this.title is a title of this instance.
     * */
    public String getTitle(){return this.title;}
    /**
     *getLInk returns a link of an article instance.
     * @return this.link is a link of this instance.
     * */
    public String getLink(){return this.link;}
    /**
     *getDescription returns a description of an article instance.
     * @return this.description is a description of this instance.
     * */
    public String getDescription(){return this.description;}
    /**
     *getPubDate returns a pub date of an article instance.
     * @return this.pubDate is a pub date of this instance.
     * */
    public String getPubDate(){return this.pubDate;}
    /**
     *toString returns a title of an article instance.
     * @return title is a title of this instance.
     * */
    public String toString(){
        return title;
    }
    /**
     *setTitle sets a string as this title.
     * @param title is a string.
     * */
    public void setTitle(String title){this.title=title;}
    /**
     *setDescription sets a string as this description.
     * @param description is a string.
     * */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     *setLink sets a string as this link.
     * @param link is a string.
     * */
    public void setLink(String link) {
        this.link = link;
    }
    /**
     *setDate sets a string as this pubDate.
     * @param pubDate is a string.
     * */
    public void setDate(String pubDate) {
        this.pubDate = pubDate;
    }
    /**
     *getId returns an id of an instance.
     * */
    public long getId() {
        return id;
    }
    /**
     *setId sets a long type value as this id.
     * @param id is a long value.
     * */
    public void setId(long id) {
        this.id = id;
    }
    /**
     *setThumbnailUrl sets a byte array as this thumbnailUrl.
     * @param url is a byte array.
     * */
    public void setThumbnailUrl(byte[] url) {
        this.thumbnailUrl = url;
    }
    /**
     *getThumbnailUrl returns this thumbnailUrl.
     * @return thumbnailUrl.
     * */
    public byte[] getThumbnailUrl() {
        return this.thumbnailUrl;
    }
}
