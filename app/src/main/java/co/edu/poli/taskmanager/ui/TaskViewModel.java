package co.edu.poli.taskmanager.ui;

import android.app.Application;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

import co.edu.poli.taskmanager.data.Category;
import co.edu.poli.taskmanager.data.Task;
import co.edu.poli.taskmanager.data.TaskRepository;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository repository;
    private final MutableLiveData<TaskFilter> filter = new MutableLiveData<>(TaskFilter.all());
    private final LiveData<List<Task>> visibleTasks;
    private final LiveData<List<Category>> categories;
    private final MediatorLiveData<List<MenuOption>> menuOptions = new MediatorLiveData<>();
    private final MutableLiveData<Task> editingTask = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> showingForm = new MutableLiveData<>(false);

    private List<Category> latestCategories = new ArrayList<>();
    private int totalCount;
    private int pendingCount;
    private int completedCount;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        categories = repository.observeCategories();
        visibleTasks = Transformations.switchMap(filter, current -> {
            switch (current.kind) {
                case PENDING:
                    return repository.observePending();
                case COMPLETED:
                    return repository.observeCompleted();
                case CATEGORY:
                    return repository.observeByCategory(current.categoryId);
                case ALL:
                default:
                    return repository.observeAllTasks();
            }
        });

        LiveData<Integer> totals = repository.observeTotalCount();
        LiveData<Integer> pending = repository.observePendingCount();
        LiveData<Integer> completed = repository.observeCompletedCount();

        menuOptions.addSource(categories, value -> {
            latestCategories = value != null ? value : new ArrayList<>();
            rebuildMenu();
        });
        menuOptions.addSource(totals, value -> {
            totalCount = value != null ? value : 0;
            rebuildMenu();
        });
        menuOptions.addSource(pending, value -> {
            pendingCount = value != null ? value : 0;
            rebuildMenu();
        });
        menuOptions.addSource(completed, value -> {
            completedCount = value != null ? value : 0;
            rebuildMenu();
        });
    }

    private void rebuildMenu() {
        List<MenuOption> options = new ArrayList<>();
        options.add(new MenuOption(TaskFilter.all(), Color.parseColor("#63B3ED"), totalCount));
        options.add(new MenuOption(TaskFilter.pending(), Color.parseColor("#ED8936"), pendingCount));
        options.add(new MenuOption(TaskFilter.completed(), Color.parseColor("#48BB78"), completedCount));
        for (Category category : latestCategories) {
            options.add(new MenuOption(
                    TaskFilter.category(category.id, category.name),
                    category.color,
                    0
            ));
        }
        menuOptions.setValue(options);
    }

    public LiveData<List<Task>> getVisibleTasks() {
        return visibleTasks;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<List<MenuOption>> getMenuOptions() {
        return menuOptions;
    }

    public LiveData<TaskFilter> getFilter() {
        return filter;
    }

    public LiveData<Boolean> getShowingForm() {
        return showingForm;
    }

    public LiveData<Task> getEditingTask() {
        return editingTask;
    }

    public void selectFilter(TaskFilter selected) {
        filter.setValue(selected);
        showingForm.setValue(false);
        editingTask.setValue(null);
    }

    public void startCreate() {
        editingTask.setValue(null);
        showingForm.setValue(true);
    }

    public void startEdit(Task task) {
        editingTask.setValue(task);
        showingForm.setValue(true);
    }

    public void cancelForm() {
        showingForm.setValue(false);
        editingTask.setValue(null);
    }

    public void saveTask(String title, String description, long categoryId, int priority, long dueDate) {
        Task existing = editingTask.getValue();
        long now = System.currentTimeMillis();
        if (existing == null) {
            repository.insert(new Task(title, description, categoryId, priority, false, dueDate, now));
        } else {
            existing.title = title;
            existing.description = description;
            existing.categoryId = categoryId;
            existing.priority = priority;
            existing.dueDate = dueDate;
            repository.update(existing);
        }
        showingForm.setValue(false);
        editingTask.setValue(null);
    }

    public void toggleCompleted(Task task) {
        task.completed = !task.completed;
        repository.update(task);
    }

    public void deleteTask(Task task) {
        repository.delete(task);
        if (Boolean.TRUE.equals(showingForm.getValue())) {
            cancelForm();
        }
    }

    public Category findCategory(long id) {
        for (Category category : latestCategories) {
            if (category.id == id) {
                return category;
            }
        }
        return null;
    }
}
