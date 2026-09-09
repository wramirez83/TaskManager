package co.edu.poli.taskmanager.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import co.edu.poli.taskmanager.util.DateUtils;

public class TaskRepository {

    private final CategoryDao categoryDao;
    private final TaskDao taskDao;
    private final PhotoDao photoDao;

    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        categoryDao = db.categoryDao();
        taskDao = db.taskDao();
        photoDao = db.photoDao();
    }

    public LiveData<List<Category>> observeCategories() {
        return categoryDao.observeAll();
    }

    public LiveData<List<Task>> observeAllTasks() {
        return taskDao.observeAll();
    }

    public LiveData<List<Task>> observeCompleted() {
        return taskDao.observeCompleted();
    }

    public LiveData<List<Task>> observeToday() {
        long start = DateUtils.startOfDay(System.currentTimeMillis());
        return taskDao.observeDueBetween(start, DateUtils.endOfDay(start));
    }

    public LiveData<List<Task>> observeImportant() {
        return taskDao.observeByPriority(Task.PRIORITY_HIGH);
    }

    public LiveData<List<Task>> observeSearch(String query) {
        return taskDao.observeSearch(query);
    }

    public LiveData<List<Photo>> observePhotos() {
        return photoDao.observeAll();
    }

    public void insert(Task task) {
        AppDatabase.IO.execute(() -> taskDao.insert(task));
    }

    public void update(Task task) {
        AppDatabase.IO.execute(() -> taskDao.update(task));
    }

    public void delete(Task task) {
        AppDatabase.IO.execute(() -> taskDao.delete(task));
    }

    public void insertPhoto(Photo photo) {
        AppDatabase.IO.execute(() -> photoDao.insert(photo));
    }

    public void deletePhoto(Photo photo) {
        AppDatabase.IO.execute(() -> photoDao.delete(photo));
    }
}
