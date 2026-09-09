package co.edu.poli.taskmanager.data;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Category.class, Task.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public static final ExecutorService IO = Executors.newSingleThreadExecutor();

    public abstract CategoryDao categoryDao();

    public abstract TaskDao taskDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "task_manager.db"
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(SEED)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Callback SEED = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            IO.execute(() -> {
                AppDatabase database = INSTANCE;
                if (database == null) {
                    return;
                }
                CategoryDao categories = database.categoryDao();
                TaskDao tasks = database.taskDao();

                long work = categories.insert(new Category("Trabajo", Color.parseColor("#2B6CB0")));
                long personal = categories.insert(new Category("Personal", Color.parseColor("#38A169")));
                long study = categories.insert(new Category("Estudio", Color.parseColor("#D69E2E")));
                long university = categories.insert(new Category("Universidad", Color.parseColor("#805AD5")));

                long now = System.currentTimeMillis();
                long day = 24L * 60L * 60L * 1000L;

                tasks.insert(new Task(
                        "Entregar informe de avance",
                        "Redactar y subir el informe de la semana 3 al aula virtual.",
                        university, Task.PRIORITY_HIGH, false, now + day, now
                ));
                tasks.insert(new Task(
                        "Revisar prototipo en Figma",
                        "Validar la arquitectura de dos paneles y los estados de cada tarea.",
                        work, Task.PRIORITY_MEDIUM, false, now + (2 * day), now
                ));
                tasks.insert(new Task(
                        "Organizar pendientes personales",
                        "Separar compras, citas y recordatorios de la semana.",
                        personal, Task.PRIORITY_LOW, true, now - day, now - (2 * day)
                ));
                tasks.insert(new Task(
                        "Estudiar Fragments y LiveData",
                        "Repasar el ciclo de vida de los fragmentos y la observación de Room.",
                        study, Task.PRIORITY_HIGH, false, now + (3 * day), now
                ));
            });
        }
    };
}
