package co.edu.poli.taskmanager.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

import co.edu.poli.taskmanager.data.Category;
import co.edu.poli.taskmanager.data.Photo;
import co.edu.poli.taskmanager.data.Task;
import co.edu.poli.taskmanager.data.TaskRepository;
import co.edu.poli.taskmanager.util.DateUtils;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository repository;
    private final MutableLiveData<TaskFilter> filter = new MutableLiveData<>(TaskFilter.all());
    private final LiveData<List<Task>> visibleTasks;
    private final LiveData<List<Category>> categories;
    private final LiveData<List<Photo>> photos;
    private final MediatorLiveData<TaskStats> stats = new MediatorLiveData<>();
    private final MutableLiveData<Task> editingTask = new MutableLiveData<>(null);

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        categories = repository.observeCategories();
        photos = repository.observePhotos();
        visibleTasks = Transformations.switchMap(filter, current -> {
            switch (current.kind) {
                case TODAY:
                    return repository.observeToday();
                case IMPORTANT:
                    return repository.observeImportant();
                case COMPLETED:
                    return repository.observeCompleted();
                case SEARCH:
                    return repository.observeSearch(current.query);
                case ALL:
                default:
                    return repository.observeAllTasks();
            }
        });
        stats.addSource(repository.observeAllTasks(), this::rebuildStats);
    }

    private void rebuildStats(List<Task> tasks) {
        TaskStats result = new TaskStats();
        if (tasks == null) {
            stats.setValue(result);
            return;
        }
        long start = DateUtils.startOfDay(System.currentTimeMillis());
        long end = DateUtils.endOfDay(start);
        result.total = tasks.size();
        for (Task task : tasks) {
            if (task.completed && task.completedAt >= start && task.completedAt < end) {
                result.completedToday++;
            }
            if (task.priority == Task.PRIORITY_HIGH) {
                result.high++;
            } else if (task.priority == Task.PRIORITY_MEDIUM) {
                result.medium++;
            } else {
                result.low++;
            }
            if (!task.completed && task.dueDate > 0 && task.dueDate < start) {
                result.overdue++;
            }
        }
        stats.setValue(result);
    }

    public LiveData<List<Task>> getVisibleTasks() {
        return visibleTasks;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<List<Photo>> getPhotos() {
        return photos;
    }

    public LiveData<TaskStats> getStats() {
        return stats;
    }

    public LiveData<TaskFilter> getFilter() {
        return filter;
    }

    public LiveData<Task> getEditingTask() {
        return editingTask;
    }

    public void selectFilter(TaskFilter selected) {
        filter.setValue(selected);
        editingTask.setValue(null);
    }

    public void startCreate() {
        editingTask.setValue(null);
    }

    public void startEdit(Task task) {
        editingTask.setValue(task);
    }

    public void saveTask(String title, String description, long categoryId, int priority, long dueDate) {
        Task existing = editingTask.getValue();
        long now = System.currentTimeMillis();
        if (existing == null) {
            repository.insert(new Task(title, description, categoryId, priority, false, dueDate, now, 0));
        } else {
            existing.title = title;
            existing.description = description;
            existing.categoryId = categoryId;
            existing.priority = priority;
            existing.dueDate = dueDate;
            repository.update(existing);
        }
        editingTask.setValue(null);
    }

    public void toggleCompleted(Task task) {
        task.completed = !task.completed;
        task.completedAt = task.completed ? System.currentTimeMillis() : 0;
        repository.update(task);
    }

    public void deleteTask(Task task) {
        repository.delete(task);
        Task editing = editingTask.getValue();
        if (editing != null && editing.id == task.id) {
            editingTask.setValue(null);
        }
    }

    public void addPhoto(String uri) {
        repository.insertPhoto(new Photo(uri, System.currentTimeMillis()));
    }

    public Category findCategory(long id) {
        List<Category> list = categories.getValue();
        if (list == null) {
            return null;
        }
        for (Category category : list) {
            if (category.id == id) {
                return category;
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }
}
