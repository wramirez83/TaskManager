package co.edu.poli.taskmanager.ui;

public class MenuOption {

    public final TaskFilter filter;
    public final int color;
    public int count;

    public MenuOption(TaskFilter filter, int color, int count) {
        this.filter = filter;
        this.color = color;
        this.count = count;
    }
}
