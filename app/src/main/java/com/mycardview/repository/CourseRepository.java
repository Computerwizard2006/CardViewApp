package com.mycardview.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mycardview.database.AppDatabase;
import com.mycardview.database.BookmarkDao;
import com.mycardview.database.BookmarkEntity;
import com.mycardview.database.ProgressDao;
import com.mycardview.database.ProgressEntity;
import com.mycardview.model.Course;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CourseRepository {

    private BookmarkDao bookmarkDao;
    private ProgressDao progressDao;
    private ExecutorService executor;

    public CourseRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        bookmarkDao = db.bookmarkDao();
        progressDao = db.progressDao();
        executor = Executors.newSingleThreadExecutor();
    }

    // ─── Course Data ───────────────────────────────────────────────────────────

    public List<Course> getAllCourses() {
        return Arrays.asList(
            new Course(1, "Ethical Hacking Basics", "Learn penetration testing from scratch with real-world labs.", "Security", "img", 12, "8h 30m", "Beginner"),
            new Course(2, "Machine Learning A-Z", "Complete ML guide with Python, NumPy, and scikit-learn.", "AI", "img_1", 20, "14h", "Intermediate"),
            new Course(3, "Android Development", "Build modern Android apps using Java and Material Design 3.", "Dev", "img_2", 15, "10h", "Beginner"),
            new Course(4, "Network Security Pro", "Deep dive into firewalls, VPNs, and intrusion detection.", "Security", "img_1", 18, "12h", "Advanced"),
            new Course(5, "Deep Learning with TensorFlow", "Neural networks, CNNs, and RNNs for real AI applications.", "AI", "img_2", 22, "16h", "Advanced"),
            new Course(6, "Flutter & Dart Masterclass", "Cross-platform app development from zero to hero.", "Dev", "img", 25, "18h", "Intermediate"),
            new Course(7, "Web Penetration Testing", "Hack and secure websites ethically using OWASP techniques.", "Security", "img_2", 14, "9h", "Intermediate"),
            new Course(8, "Python for Data Science", "Data analysis, visualization, and ML with Python.", "AI", "img", 16, "11h", "Beginner"),
            new Course(9, "React Native Apps", "Build iOS and Android apps with JavaScript.", "Dev", "img_1", 20, "13h", "Intermediate")
        );
    }

    // ─── Bookmarks ─────────────────────────────────────────────────────────────

    public void addBookmark(BookmarkEntity bookmark) {
        executor.execute(() -> bookmarkDao.insertBookmark(bookmark));
    }

    public void removeBookmark(int courseId) {
        executor.execute(() -> bookmarkDao.deleteBookmarkById(courseId));
    }

    public LiveData<List<BookmarkEntity>> getAllBookmarks() {
        return bookmarkDao.getAllBookmarks();
    }

    public void isBookmarked(int courseId, BookmarkCallback callback) {
        executor.execute(() -> {
            int count = bookmarkDao.isBookmarked(courseId);
            callback.onResult(count > 0);
        });
    }

    // ─── Progress ──────────────────────────────────────────────────────────────

    public void updateProgress(int courseId, int percent, int completedModules) {
        executor.execute(() -> {
            ProgressEntity progress = new ProgressEntity(courseId, percent, completedModules);
            progressDao.insertOrUpdateProgress(progress);
        });
    }

    public LiveData<ProgressEntity> getCourseProgress(int courseId) {
        return progressDao.getProgressByCourse(courseId);
    }

    public LiveData<Float> getOverallProgress() {
        return progressDao.getOverallProgress();
    }

    public interface BookmarkCallback {
        void onResult(boolean isBookmarked);
    }
}
