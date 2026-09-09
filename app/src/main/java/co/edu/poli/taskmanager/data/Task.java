package co.edu.poli.taskmanager.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "tasks",
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "categoryId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("categoryId")}
)
public class Task {

    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_MEDIUM = 1;
    public static final int PRIORITY_HIGH = 2;

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String title = "";

    @NonNull
    public String description = "";

    public long categoryId;
    public int priority;
    public boolean completed;
    public long dueDate;
    public long createdAt;
    public long completedAt;

    public Task() {
    }

    @Ignore
    public Task(@NonNull String title, @NonNull String description, long categoryId,
                int priority, boolean completed, long dueDate, long createdAt, long completedAt) {
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.priority = priority;
        this.completed = completed;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }
}
