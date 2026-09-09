package co.edu.poli.taskmanager.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name;

    public int color;

    public Category() {
        this.name = "";
    }

    @Ignore
    public Category(@NonNull String name, int color) {
        this.name = name;
        this.color = color;
    }
}
