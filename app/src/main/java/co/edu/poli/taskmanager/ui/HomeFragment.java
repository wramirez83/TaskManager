package co.edu.poli.taskmanager.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        bindCard(binding.cardAll, R.drawable.ic_document, R.string.all, TaskFilter.all());
        bindCard(binding.cardToday, R.drawable.ic_calendar, R.string.today, TaskFilter.today());
        bindCard(binding.cardImportant, R.drawable.ic_flag, R.string.important, TaskFilter.important());
        bindCard(binding.cardCompleted, R.drawable.ic_check, R.string.completed, TaskFilter.completed());
    }

    private void bindCard(ItemHomeCategoryBinding card, int icon, int title, TaskFilter filter) {
        card.categoryIcon.setImageResource(icon);
        card.categoryTitle.setText(title);
        card.getRoot().setOnClickListener(v -> ((MainActivity) requireActivity()).openTasks(filter));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
