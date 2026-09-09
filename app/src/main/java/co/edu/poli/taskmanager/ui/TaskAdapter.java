package co.edu.poli.taskmanager.ui;

import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import co.edu.poli.taskmanager.R;
import co.edu.poli.taskmanager.data.Category;
import co.edu.poli.taskmanager.data.Task;
import co.edu.poli.taskmanager.databinding.ItemTaskBinding;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    public interface Listener {
        void onToggle(Task task);

        void onEdit(Task task);

        void onDelete(Task task);
    }

    private final List<Task> items = new ArrayList<>();
    private final Listener listener;
    private final Function<Long, Category> categoryLookup;

    public TaskAdapter(Listener listener, Function<Long, Category> categoryLookup) {
        this.listener = listener;
        this.categoryLookup = categoryLookup;
    }

    public void submit(List<Task> tasks) {
        items.clear();
        if (tasks != null) {
            items.addAll(tasks);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskHolder(ItemTaskBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class TaskHolder extends RecyclerView.ViewHolder {
        private final ItemTaskBinding binding;

        TaskHolder(ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Task task) {
            binding.taskTitle.setText(task.title);
            binding.taskDescription.setText(task.description);
            binding.taskCheck.setOnCheckedChangeListener(null);
            binding.taskCheck.setChecked(task.completed);

            int flags = binding.taskTitle.getPaintFlags();
            if (task.completed) {
                binding.taskTitle.setPaintFlags(flags | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                binding.taskTitle.setPaintFlags(flags & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }

            Category category = categoryLookup.apply(task.categoryId);
            if (category != null) {
                binding.taskCategory.setText(category.name);
                GradientDrawable chip = (GradientDrawable) binding.taskCategory.getBackground().mutate();
                chip.setColor(adjustAlpha(category.color, 30));
                binding.taskCategory.setTextColor(category.color);
            }

            binding.taskPriority.setText(priorityLabel(task.priority));
            binding.taskPriority.setBackgroundColor(priorityColor(task.priority));
            binding.taskDue.setText(task.dueDate > 0
                    ? DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(task.dueDate))
                    : itemView.getContext().getString(R.string.no_due_date));

            binding.taskCheck.setOnCheckedChangeListener((button, checked) -> listener.onToggle(task));
            itemView.setOnClickListener(v -> listener.onEdit(task));
            binding.buttonDelete.setOnClickListener(v -> listener.onDelete(task));
        }

        private String priorityLabel(int priority) {
            if (priority == Task.PRIORITY_HIGH) {
                return "Alta";
            }
            if (priority == Task.PRIORITY_LOW) {
                return "Baja";
            }
            return "Media";
        }

        private int priorityColor(int priority) {
            if (priority == Task.PRIORITY_HIGH) {
                return ContextCompat.getColor(itemView.getContext(), R.color.priority_high);
            }
            if (priority == Task.PRIORITY_LOW) {
                return ContextCompat.getColor(itemView.getContext(), R.color.priority_low);
            }
            return ContextCompat.getColor(itemView.getContext(), R.color.priority_medium);
        }

        private int adjustAlpha(int color, int alpha) {
            return (color & 0x00FFFFFF) | (alpha << 24);
        }
    }
}
