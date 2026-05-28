package com.mycardview.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarks")
public class BookmarkEntity {

    @PrimaryKey
    public int courseId;
    public String title;
    public String category;
    public String imageUrl;
    public String duration;
    public String difficulty;
    public long savedAt;

    public BookmarkEntity(int courseId, String title, String category,
                          String imageUrl, String duration, String difficulty) {
        this.courseId = courseId;
        this.title = title;
        this.category = category;
        this.imageUrl = imageUrl;
        this.duration = duration;
        this.difficulty = difficulty;
        this.savedAt = System.currentTimeMillis();
    }
}
