package com.mycardview.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBookmark(BookmarkEntity bookmark);

    @Delete
    void deleteBookmark(BookmarkEntity bookmark);

    @Query("SELECT * FROM bookmarks ORDER BY savedAt DESC")
    LiveData<List<BookmarkEntity>> getAllBookmarks();

    @Query("SELECT COUNT(*) FROM bookmarks WHERE courseId = :courseId")
    int isBookmarked(int courseId);

    @Query("DELETE FROM bookmarks WHERE courseId = :courseId")
    void deleteBookmarkById(int courseId);
}
