package co.edu.poli.taskmanager.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {

    private final CategoryDao categoryDao;
    private final TaskDao taskDao;

    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        categoryDao = db.categoryDao();
        taskDao = db.taskDao();
    }

    public LiveData<List<Category>> observeCategories() {
        return categoryDao.observeAll();
    }

    public LiveData<List<Task>> observeAllTasks() {
        return taskDao.observeAll();
    }

    public LiveData<List<Task>> observePending() {
        return taskDao.observePending();
    }

    public LiveData<List<Task>> observeCompleted() {
        return taskDao.observeCompleted();
    }

    public LiveData<List<Task>> observeByCategory(long categoryId) {
        return taskDao.observeByCategory(categoryId);
    }

    public LiveData<Integer> observeTotalCount() {
        return taskDao.observeTotalCount();
    }

    public LiveData<Integer> observePendingCount() {
        return taskDao.observePendingCount();
    }

    public LiveData<Integer> observeCompletedCount() {
        return taskDao.observeCompletedCount();
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

    public void insertCategory(Category category) {
        AppDatabase.IO.execute(() -> categoryDao.insert(category));
    }
}
