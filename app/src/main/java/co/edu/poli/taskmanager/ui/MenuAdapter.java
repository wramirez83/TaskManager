package co.edu.poli.taskmanager.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.edu.poli.taskmanager.R;
import co.edu.poli.taskmanager.databinding.ItemCategoryBinding;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

    public interface OnMenuClick {
        void onMenuClick(MenuOption option);
    }

    private final List<MenuOption> items = new ArrayList<>();
    private final OnMenuClick listener;
    private TaskFilter selected = TaskFilter.all();

    public MenuAdapter(OnMenuClick listener) {
        this.listener = listener;
    }

    public void submit(List<MenuOption> options) {
        items.clear();
        if (options != null) {
            items.addAll(options);
        }
        notifyDataSetChanged();
    }

    public void setSelected(TaskFilter filter) {
        selected = filter;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuHolder(ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MenuHolder extends RecyclerView.ViewHolder {
        private final ItemCategoryBinding binding;

        MenuHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MenuOption option) {
            binding.itemTitle.setText(option.filter.title);
            boolean isSelected = selected != null && selected.matches(option.filter);
            binding.getRoot().setSelected(isSelected);
            binding.itemTitle.setTextColor(isSelected ? Color.WHITE : Color.parseColor("#D0D9E6"));

            if (option.filter.kind == TaskFilter.Kind.CATEGORY) {
                binding.itemCount.setVisibility(android.view.View.GONE);
            } else {
                binding.itemCount.setVisibility(android.view.View.VISIBLE);
                binding.itemCount.setText(String.valueOf(option.count));
            }

            GradientDrawable dot = (GradientDrawable) binding.itemDot.getBackground().mutate();
            dot.setColor(option.color);
            binding.itemAccent.setBackgroundColor(isSelected
                    ? itemView.getResources().getColor(R.color.accent, itemView.getContext().getTheme())
                    : Color.TRANSPARENT);

            itemView.setOnClickListener(v -> listener.onMenuClick(option));
        }
    }
}
