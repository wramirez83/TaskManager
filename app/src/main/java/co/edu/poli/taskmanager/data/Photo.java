package co.edu.poli.taskmanager.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "photos")
public class Photo {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String uri = "";

    public long createdAt;

    public Photo() {
    }

    @Ignore
    public Photo(@NonNull String uri, long createdAt) {
        this.uri = uri;
        this.createdAt = createdAt;
    }
}
