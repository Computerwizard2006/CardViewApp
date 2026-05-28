package com.example.mycardview;

public class CardModel {
    private String title, desc, category;
    private int image;
    private boolean isFavorite = false;

    public CardModel(String title, String desc, int image, String category) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.category = category;
    }

    public String getTitle() { return title; }
    public String getDesc() { return desc; }
    public int getImage() { return image; }
    public String getCategory() { return category; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}