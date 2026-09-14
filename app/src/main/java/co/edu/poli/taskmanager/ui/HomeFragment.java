package co.edu.poli.taskmanager.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import co.edu.poli.taskmanager.MainActivity;
import co.edu.poli.taskmanager.R;
import co.edu.poli.taskmanager.databinding.FragmentHomeBinding;
import co.edu.poli.taskmanager.databinding.ItemHomeCategoryBinding;
import co.edu.poli.taskmanager.util.DateUtils;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.greetingDate.setText(DateUtils.greetingDate());
        bindCard(binding.cardAll, R.drawable.ic_layers, R.string.all, TaskFilter.all(),
                R.drawable.bg_icon_blue, R.color.primary);
        bindCard(binding.cardToday, R.drawable.ic_calendar, R.string.today, TaskFilter.today(),
                R.drawable.bg_icon_blue, R.color.primary);
        bindCard(binding.cardImportant, R.drawable.ic_flag, R.string.important, TaskFilter.important(),
                R.drawable.bg_icon_red, R.color.priority_high);
        bindCard(binding.cardCompleted, R.drawable.ic_check_circle, R.string.completed, TaskFilter.completed(),
                R.drawable.bg_icon_green, R.color.priority_low);

        TaskViewModel viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        viewModel.getStats().observe(getViewLifecycleOwner(), stats -> {
            if (stats == null || binding == null) {
                return;
            }
            binding.cardAll.categoryCount.setText(String.valueOf(stats.total));
            binding.cardToday.categoryCount.setText(String.valueOf(stats.today));
            binding.cardImportant.categoryCount.setText(String.valueOf(stats.important));
            binding.cardCompleted.categoryCount.setText(String.valueOf(stats.completed));
        });
    }

    private void bindCard(ItemHomeCategoryBinding card, @DrawableRes int icon, int title,
                          TaskFilter filter, @DrawableRes int iconBg, int tintColor) {
        card.categoryIcon.setImageResource(icon);
        card.categoryIcon.setColorFilter(ContextCompat.getColor(requireContext(), tintColor));
        card.categoryIconBg.setBackgroundResource(iconBg);
        card.categoryTitle.setText(title);
        card.getRoot().setOnClickListener(v -> ((MainActivity) requireActivity()).openTasks(filter));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
