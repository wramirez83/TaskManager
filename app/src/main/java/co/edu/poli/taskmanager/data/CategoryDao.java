package co.edu.poli.taskmanager.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY name ASC")
    LiveData<List<Category>> observeAll();

    @Query("SELECT * FROM categories ORDER BY name ASC")
    List<Category> getAllSync();

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    Category findById(long id);

    @Query("SELECT COUNT(*) FROM categories")
    int countSync();

    @Insert
    long insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);
}
