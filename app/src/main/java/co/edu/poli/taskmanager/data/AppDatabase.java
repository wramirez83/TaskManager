package co.edu.poli.taskmanager.data;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import co.edu.poli.taskmanager.util.DateUtils;

@Database(entities = {Category.class, Task.class, Photo.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public static final ExecutorService IO = Executors.newSingleThreadExecutor();

    public abstract CategoryDao categoryDao();

    public abstract TaskDao taskDao();

    public abstract PhotoDao photoDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    Context app = context.getApplicationContext();
                    INSTANCE = Room.databaseBuilder(app, AppDatabase.class, "task_manager.db")
                            .fallbackToDestructiveMigration()
                            .build();
                    IO.execute(() -> INSTANCE.ensureDefaults(app));
                }
            }
        }
        return INSTANCE;
    }

    private void ensureDefaults(@NonNull Context context) {
        runInTransaction(() -> {
            if (categoryDao().countSync() == 0) {
                seedCategoriesAndTasks();
            }
            if (photoDao().countSync() == 0) {
                seedPhotos(context);
            }
        });
    }

    private void seedCategoriesAndTasks() {
        CategoryDao categories = categoryDao();
        TaskDao tasks = taskDao();

        long work = categories.insert(new Category("Trabajo", Color.parseColor("#007AFF")));
        long personal = categories.insert(new Category("Personal", Color.parseColor("#007AFF")));
        long urgent = categories.insert(new Category("Urgente", Color.parseColor("#FF3B30")));
        long study = categories.insert(new Category("Estudio", Color.parseColor("#007AFF")));

        long now = System.currentTimeMillis();
        long today = DateUtils.startOfDay(now) + 12L * 60L * 60L * 1000L;
        long day = 24L * 60L * 60L * 1000L;

        tasks.insert(new Task("Revisar prototipo en Figma", "Validar las 7 pantallas del diseño.", work, Task.PRIORITY_HIGH, false, today, now, 0));
        tasks.insert(new Task("Entregar informe de avance", "Subir el informe de la semana 3.", study, Task.PRIORITY_HIGH, false, today + day, now, 0));
        tasks.insert(new Task("Organizar pendientes personales", "Separar compras y citas de la semana.", personal, Task.PRIORITY_LOW, true, today - day, now - (2 * day), now - 3600000));
        tasks.insert(new Task("Estudiar Fragments y LiveData", "Repasar ciclo de vida y observación de Room.", study, Task.PRIORITY_HIGH, false, today + (2 * day), now, 0));
        tasks.insert(new Task("Preparar reunión de equipo", "Agenda, avances y bloqueos.", work, Task.PRIORITY_MEDIUM, false, today, now, 0));
        tasks.insert(new Task("Actualizar tablero Kanban", "Mover tarjetas al estado correcto.", work, Task.PRIORITY_MEDIUM, true, today, now - day, now - 7200000));
        tasks.insert(new Task("Pagar factura de servicios", "Energía y plan de datos.", personal, Task.PRIORITY_MEDIUM, false, today - (2 * day), now - (3 * day), 0));
        tasks.insert(new Task("Enviar correos pendientes", "Responder a docentes y compañeros.", urgent, Task.PRIORITY_HIGH, true, today, now - day, now - 1800000));
        tasks.insert(new Task("Leer guía de productividad", "Técnica Pomodoro y gestión del tiempo.", study, Task.PRIORITY_LOW, true, today, now - day, now - 5400000));
        tasks.insert(new Task("Backup del proyecto", "Subir el código al repositorio.", work, Task.PRIORITY_MEDIUM, true, today, now - day, now - 900000));
        tasks.insert(new Task("Comprar útiles", "Cuaderno y marcadores para el taller.", personal, Task.PRIORITY_LOW, false, today + (3 * day), now, 0));
        tasks.insert(new Task("Ensayar presentación", "Demo de la app Task Manager.", urgent, Task.PRIORITY_HIGH, false, today - day, now - (2 * day), 0));
        tasks.insert(new Task("Revisar estadísticas", "Confirmar tareas vencidas y completadas.", work, Task.PRIORITY_MEDIUM, true, today, now - day, now - 1200000));
        tasks.insert(new Task("Subir fotos de evidencia", "Capturas del tablero y la reunión.", personal, Task.PRIORITY_LOW, true, today, now - day, now - 2400000));
        tasks.insert(new Task("Ver video tutorial", "Cómo organizar tus tareas.", study, Task.PRIORITY_MEDIUM, false, today + day, now, 0));
    }

    private void seedPhotos(@NonNull Context context) {
        String pkg = context.getPackageName();
        long now = System.currentTimeMillis();
        PhotoDao photos = photoDao();
        for (String res : new String[]{"photo_1", "photo_2", "photo_3", "photo_4", "photo_5", "photo_6"}) {
            photos.insert(new Photo("android.resource://" + pkg + "/drawable/" + res, now));
        }
    }
}
