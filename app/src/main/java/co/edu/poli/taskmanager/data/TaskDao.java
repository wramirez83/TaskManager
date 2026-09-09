package co.edu.poli.taskmanager.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY completed ASC, priority DESC, dueDate ASC, createdAt DESC")
    LiveData<List<Task>> observeAll();

    @Query("SELECT * FROM tasks WHERE completed = 0 ORDER BY priority DESC, dueDate ASC")
    LiveData<List<Task>> observePending();

    @Query("SELECT * FROM tasks WHERE completed = 1 ORDER BY createdAt DESC")
    LiveData<List<Task>> observeCompleted();

    @Query("SELECT * FROM tasks WHERE categoryId = :categoryId ORDER BY completed ASC, priority DESC")
    LiveData<List<Task>> observeByCategory(long categoryId);

    @Query("SELECT COUNT(*) FROM tasks")
    LiveData<Integer> observeTotalCount();

    @Query("SELECT COUNT(*) FROM tasks WHERE completed = 0")
    LiveData<Integer> observePendingCount();

    @Query("SELECT COUNT(*) FROM tasks WHERE completed = 1")
    LiveData<Integer> observeCompletedCount();

    @Query("SELECT COUNT(*) FROM tasks WHERE categoryId = :categoryId")
    LiveData<Integer> observeCountByCategory(long categoryId);

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    Task findById(long id);

    @Insert
    long insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);
}
