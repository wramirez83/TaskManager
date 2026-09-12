package co.edu.poli.taskmanager.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhotoDao {

    @Query("SELECT * FROM photos ORDER BY createdAt DESC")
    LiveData<List<Photo>> observeAll();

    @Query("SELECT COUNT(*) FROM photos")
    int countSync();

    @Insert
    long insert(Photo photo);

    @Delete
    void delete(Photo photo);
}
