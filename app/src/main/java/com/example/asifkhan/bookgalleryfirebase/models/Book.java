package com.example.asifkhan.bookgalleryfirebase.models;

public class Book {
    private String id,title,author,rating,coverPhotoURL;

    public Book(){}

    public Book(String title, String author, String rating, String coverPhotoURL) {
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.coverPhotoURL = coverPhotoURL;
    }

    public Book(String id, String title, String author, String rating, String coverPhotoURL) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.coverPhotoURL = coverPhotoURL;
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCoverPhotoURL() {
        return coverPhotoURL;
    }

    public void setCoverPhotoURL(String coverPhotoURL) {
        this.coverPhotoURL = coverPhotoURL;
    }
}
