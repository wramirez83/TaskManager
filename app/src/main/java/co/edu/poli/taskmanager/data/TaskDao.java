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

    @Query("SELECT * FROM tasks WHERE completed = 1 ORDER BY completedAt DESC")
    LiveData<List<Task>> observeCompleted();

    @Query("SELECT * FROM tasks WHERE dueDate >= :start AND dueDate < :end ORDER BY priority DESC, dueDate ASC")
    LiveData<List<Task>> observeDueBetween(long start, long end);

    @Query("SELECT * FROM tasks WHERE priority = :priority ORDER BY completed ASC, dueDate ASC")
    LiveData<List<Task>> observeByPriority(int priority);

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    LiveData<List<Task>> observeSearch(String query);

    @Insert
    long insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);
}
