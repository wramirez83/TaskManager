package co.edu.poli.taskmanager.ui;

public class TaskFilter {

    public enum Kind {
        ALL,
        TODAY,
        IMPORTANT,
        COMPLETED,
        SEARCH
    }

    public final Kind kind;
    public final String title;
    public final String query;

    private TaskFilter(Kind kind, String title, String query) {
        this.kind = kind;
        this.title = title;
        this.query = query;
    }

    public static TaskFilter all() {
        return new TaskFilter(Kind.ALL, "Lista de tareas", "");
    }

    public static TaskFilter today() {
        return new TaskFilter(Kind.TODAY, "Hoy", "");
    }

    public static TaskFilter important() {
        return new TaskFilter(Kind.IMPORTANT, "Importantes", "");
    }

    public static TaskFilter completed() {
        return new TaskFilter(Kind.COMPLETED, "Completadas", "");
    }

    public static TaskFilter search(String query) {
        return new TaskFilter(Kind.SEARCH, "Búsqueda", query);
    }
}
