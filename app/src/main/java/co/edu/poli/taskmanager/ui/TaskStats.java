package co.edu.poli.taskmanager.ui;

public class TaskStats {

    public int total;
    public int completedToday;
    public int completed;
    public int today;
    public int important;
    public int pending;
    public int high;
    public int medium;
    public int low;
    public int overdue;

    public int percent() {
        if (total == 0) {
            return 0;
        }
        return Math.round(completedToday * 100f / total);
    }
}
