package co.edu.poli.taskmanager.ui;

import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import co.edu.poli.taskmanager.R;
import co.edu.poli.taskmanager.data.Category;
import co.edu.poli.taskmanager.data.Task;
import co.edu.poli.taskmanager.databinding.ItemTaskBinding;
import co.edu.poli.taskmanager.util.DateUtils;

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
        return new TaskHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
            binding.taskDue.setText(DateUtils.shortDate(task.dueDate));
            binding.taskCheck.setOnCheckedChangeListener(null);
            binding.taskCheck.setChecked(task.completed);
            int flags = binding.taskTitle.getPaintFlags();
            binding.taskTitle.setPaintFlags(task.completed
                    ? flags | Paint.STRIKE_THRU_TEXT_FLAG
                    : flags & ~Paint.STRIKE_THRU_TEXT_FLAG);

            Category category = categoryLookup.apply(task.categoryId);
            binding.taskCategory.setText(category != null ? category.name : "");
            binding.taskPriority.setText(priorityLabel(task.priority));
            GradientDrawable badge = (GradientDrawable) ContextCompat.getDrawable(
                    itemView.getContext(), R.drawable.bg_priority);
            if (badge != null) {
                badge = (GradientDrawable) badge.mutate();
                badge.setColor(priorityColor(task.priority));
                binding.taskPriority.setBackground(badge);
            }

            binding.taskCheck.setOnCheckedChangeListener((b, c) -> listener.onToggle(task));
            itemView.setOnClickListener(v -> listener.onEdit(task));
            itemView.setOnLongClickListener(v -> {
                listener.onDelete(task);
                return true;
            });
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
    }
}
