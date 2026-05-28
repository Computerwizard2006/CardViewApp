package com.mycardview.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mycardview.database.BookmarkEntity;
import com.mycardview.model.Course;
import com.mycardview.repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private CourseRepository repository;
    private MutableLiveData<List<Course>> filteredCourses = new MutableLiveData<>();
    private MutableLiveData<String> selectedCategory = new MutableLiveData<>("All");
    private MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private LiveData<List<BookmarkEntity>> bookmarks;
    private LiveData<Float> overallProgress;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new CourseRepository(application);
        bookmarks = repository.getAllBookmarks();
        overallProgress = repository.getOverallProgress();
        loadCourses();
    }

    private void loadCourses() {
        filteredCourses.setValue(repository.getAllCourses());
    }

    public void filterCourses(String category, String query) {
        selectedCategory.setValue(category);
        searchQuery.setValue(query);

        List<Course> all = repository.getAllCourses();
        List<Course> result = new ArrayList<>();

        for (Course course : all) {
            boolean categoryMatch = category.equals("All") || course.getCategory().equals(category);
            boolean queryMatch = query.isEmpty() ||
                    course.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    course.getDescription().toLowerCase().contains(query.toLowerCase());
            if (categoryMatch && queryMatch) {
                result.add(course);
            }
        }
        filteredCourses.setValue(result);
    }

    public void toggleBookmark(Course course) {
        repository.isBookmarked(course.getId(), isBookmarked -> {
            if (isBookmarked) {
                repository.removeBookmark(course.getId());
            } else {
                BookmarkEntity entity = new BookmarkEntity(
                        course.getId(), course.getTitle(), course.getCategory(),
                        course.getImageUrl(), course.getDuration(), course.getDifficulty()
                );
                repository.addBookmark(entity);
            }
        });
    }

    public void updateProgress(int courseId, int percent, int completedModules) {
        repository.updateProgress(courseId, percent, completedModules);
    }

    public void checkBookmark(int courseId, CourseRepository.BookmarkCallback callback) {
        repository.isBookmarked(courseId, callback);
    }

    // Getters
    public LiveData<List<Course>> getFilteredCourses() { return filteredCourses; }
    public LiveData<List<BookmarkEntity>> getBookmarks() { return bookmarks; }
    public LiveData<Float> getOverallProgress() { return overallProgress; }
    public String getSelectedCategory() { return selectedCategory.getValue(); }
}
