package com.mycardview.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateProgress(ProgressEntity progress);

    @Query("SELECT * FROM progress WHERE courseId = :courseId")
    LiveData<ProgressEntity> getProgressByCourse(int courseId);

    @Query("SELECT * FROM progress ORDER BY lastAccessedAt DESC")
    LiveData<List<ProgressEntity>> getAllProgress();

    @Query("SELECT AVG(progressPercent) FROM progress")
    LiveData<Float> getOverallProgress();
}
