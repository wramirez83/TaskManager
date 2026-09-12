package co.edu.poli.taskmanager.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import co.edu.poli.taskmanager.MainActivity;
import co.edu.poli.taskmanager.R;
import co.edu.poli.taskmanager.data.Category;
import co.edu.poli.taskmanager.data.Task;
import co.edu.poli.taskmanager.databinding.FragmentCreateTaskBinding;
import co.edu.poli.taskmanager.util.DateUtils;

public class CreateTaskFragment extends Fragment {

    private FragmentCreateTaskBinding binding;
    private TaskViewModel viewModel;
    private long selectedDueDate;
    private boolean formBound;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        binding.dateRow.setOnClickListener(v -> pickDate());
        binding.buttonSave.setOnClickListener(v -> save());
        viewModel.getCategories().observe(getViewLifecycleOwner(), this::bindCategories);
        viewModel.getEditingTask().observe(getViewLifecycleOwner(), task -> {
            if (!formBound) {
                bindTask(task);
                formBound = true;
            }
        });
    }

    private void bindCategories(List<Category> categories) {
        if (binding == null) {
            return;
        }
        binding.categoryGroup.removeAllViews();
        if (categories == null || categories.isEmpty()) {
            return;
        }
        Task editing = viewModel.getEditingTask().getValue();
        long selectedId = editing != null ? editing.categoryId : categories.get(0).id;
        for (Category category : categories) {
            Chip chip = new Chip(requireContext());
            chip.setId(View.generateViewId());
            chip.setText(category.name);
            chip.setCheckable(true);
            chip.setTag(category.id);
            chip.setChipBackgroundColorResource(R.color.primary_soft);
            chip.setChipStrokeColorResource(R.color.primary);
            chip.setChipStrokeWidth(2f);
            chip.setTextColor(requireContext().getColor(R.color.primary));
            binding.categoryGroup.addView(chip);
            chip.setChecked(category.id == selectedId);
        }
    }

    private void bindTask(Task task) {
        if (binding == null) {
            return;
        }
        if (task == null) {
            selectedDueDate = 0;
            binding.inputTitle.setText("");
            binding.inputDescription.setText("");
            binding.inputDate.setText(R.string.select_date);
            binding.chipHigh.setChecked(true);
            return;
        }
        binding.inputTitle.setText(task.title);
        binding.inputDescription.setText(task.description);
        selectedDueDate = task.dueDate;
        binding.inputDate.setText(task.dueDate > 0
                ? DateUtils.shortDate(task.dueDate)
                : getString(R.string.select_date));
        if (task.priority == Task.PRIORITY_HIGH) {
            binding.chipHigh.setChecked(true);
        } else if (task.priority == Task.PRIORITY_LOW) {
            binding.chipLow.setChecked(true);
        } else {
            binding.chipMedium.setChecked(true);
        }
    }

    private void pickDate() {
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.date)
                .setSelection(selectedDueDate > 0 ? selectedDueDate : MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        picker.addOnPositiveButtonClickListener(selection -> {
            if (binding == null) {
                return;
            }
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            selectedDueDate = calendar.getTimeInMillis();
            binding.inputDate.setText(DateUtils.shortDate(selectedDueDate));
        });
        picker.show(getParentFragmentManager(), "dueDate");
    }

    private void save() {
        if (binding == null) {
            return;
        }
        String title = String.valueOf(binding.inputTitle.getText()).trim();
        if (title.isEmpty()) {
            binding.inputTitle.setError(getString(R.string.title_required));
            return;
        }
        long categoryId = selectedCategoryId();
        if (categoryId <= 0) {
            Toast.makeText(requireContext(), R.string.category_required, Toast.LENGTH_SHORT).show();
            return;
        }
        String description = String.valueOf(binding.inputDescription.getText()).trim();
        int priority = Task.PRIORITY_MEDIUM;
        if (binding.chipHigh.isChecked()) {
            priority = Task.PRIORITY_HIGH;
        } else if (binding.chipLow.isChecked()) {
            priority = Task.PRIORITY_LOW;
        }
        viewModel.saveTask(title, description, categoryId, priority, selectedDueDate);
        Toast.makeText(requireContext(), R.string.task_saved, Toast.LENGTH_SHORT).show();
        ((MainActivity) requireActivity()).openTasks(TaskFilter.all());
    }

    private long selectedCategoryId() {
        int checkedId = binding.categoryGroup.getCheckedChipId();
        View checked = binding.categoryGroup.findViewById(checkedId);
        if (checked != null && checked.getTag() instanceof Number) {
            return ((Number) checked.getTag()).longValue();
        }
        for (int i = 0; i < binding.categoryGroup.getChildCount(); i++) {
            Chip chip = (Chip) binding.categoryGroup.getChildAt(i);
            if (chip.isChecked() && chip.getTag() instanceof Number) {
                return ((Number) chip.getTag()).longValue();
            }
        }
        List<Category> categories = viewModel.getCategories().getValue();
        if (categories != null && !categories.isEmpty()) {
            return categories.get(0).id;
        }
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
