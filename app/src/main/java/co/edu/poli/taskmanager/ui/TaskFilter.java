package co.edu.poli.taskmanager.ui;

public class TaskFilter {

    public enum Kind {
        ALL,
        PENDING,
        COMPLETED,
        CATEGORY
    }

    public final Kind kind;
    public final long categoryId;
    public final String title;

    private TaskFilter(Kind kind, long categoryId, String title) {
        this.kind = kind;
        this.categoryId = categoryId;
        this.title = title;
    }

    public static TaskFilter all() {
        return new TaskFilter(Kind.ALL, -1, "Todas las tareas");
    }

    public static TaskFilter pending() {
        return new TaskFilter(Kind.PENDING, -1, "Pendientes");
    }

    public static TaskFilter completed() {
        return new TaskFilter(Kind.COMPLETED, -1, "Completadas");
    }

    public static TaskFilter category(long categoryId, String name) {
        return new TaskFilter(Kind.CATEGORY, categoryId, name);
    }

    public boolean matches(TaskFilter other) {
        return other != null && kind == other.kind && categoryId == other.categoryId;
    }
}
