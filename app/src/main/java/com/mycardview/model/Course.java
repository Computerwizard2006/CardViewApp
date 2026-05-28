package com.mycardview.model;

public class Course {
    private int id;
    private String title;
    private String description;
    private String category;
    private String imageUrl;
    private int moduleCount;
    private String duration;
    private int progress; // 0-100
    private boolean isBookmarked;
    private String difficulty; // Beginner, Intermediate, Advanced

    public Course(int id, String title, String description, String category,
                  String imageUrl, int moduleCount, String duration, String difficulty) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.moduleCount = moduleCount;
        this.duration = duration;
        this.difficulty = difficulty;
        this.progress = 0;
        this.isBookmarked = false;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
    public int getModuleCount() { return moduleCount; }
    public String getDuration() { return duration; }
    public int getProgress() { return progress; }
    public boolean isBookmarked() { return isBookmarked; }
    public String getDifficulty() { return difficulty; }

    // Setters
    public void setProgress(int progress) { this.progress = progress; }
    public void setBookmarked(boolean bookmarked) { isBookmarked = bookmarked; }
}
