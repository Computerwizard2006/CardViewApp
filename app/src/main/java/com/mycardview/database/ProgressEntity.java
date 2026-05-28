package com.mycardview.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "progress")
public class ProgressEntity {

    @PrimaryKey
    public int courseId;
    public int progressPercent; // 0-100
    public int completedModules;
    public long lastAccessedAt;

    public ProgressEntity(int courseId, int progressPercent, int completedModules) {
        this.courseId = courseId;
        this.progressPercent = progressPercent;
        this.completedModules = completedModules;
        this.lastAccessedAt = System.currentTimeMillis();
    }
}
