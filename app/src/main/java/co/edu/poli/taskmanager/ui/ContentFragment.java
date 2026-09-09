package co.edu.poli.taskmanager.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import co.edu.poli.taskmanager.R;
import co.edu.poli.taskmanager.data.Category;
import co.edu.poli.taskmanager.data.Task;
import co.edu.poli.taskmanager.databinding.FragmentContentBinding;

public class ContentFragment extends Fragment {

    private FragmentContentBinding binding;
    private TaskViewModel viewModel;
    private TaskAdapter adapter;
    private final List<Category> categories = new ArrayList<>();
    private long selectedDueDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentContentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        adapter = new TaskAdapter(new TaskAdapter.Listener() {
            @Override
            public void onToggle(Task task) {
                viewModel.toggleCompleted(task);
            }

            @Override
            public void onEdit(Task task) {
                viewModel.startEdit(task);
            }

            @Override
            public void onDelete(Task task) {
                confirmDelete(task);
            }
        }, viewModel::findCategory);

        binding.taskList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.taskList.setAdapter(adapter);
        binding.fabAdd.setOnClickListener(v -> viewModel.startCreate());
        binding.buttonCancel.setOnClickListener(v -> viewModel.cancelForm());
        binding.buttonSave.setOnClickListener(v -> persistForm());
        binding.inputDueDate.setOnClickListener(v -> pickDueDate());

        binding.inputPriority.setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                new String[]{"Baja", "Media", "Alta"}
        ));

        viewModel.getVisibleTasks().observe(getViewLifecycleOwner(), tasks -> {
            adapter.submit(tasks);
            boolean empty = tasks == null || tasks.isEmpty();
            binding.emptyState.setVisibility(empty && !Boolean.TRUE.equals(viewModel.getShowingForm().getValue())
                    ? View.VISIBLE : View.GONE);
            binding.headerCount.setText(getString(R.string.task_count, tasks == null ? 0 : tasks.size()));
        });
        viewModel.getFilter().observe(getViewLifecycleOwner(), filter -> {
            if (filter != null) {
                binding.headerTitle.setText(filter.title);
            }
        });
        viewModel.getCategories().observe(getViewLifecycleOwner(), list -> {
            categories.clear();
            if (list != null) {
                categories.addAll(list);
            }
            refreshCategorySpinner();
            adapter.notifyDataSetChanged();
        });
        viewModel.getShowingForm().observe(getViewLifecycleOwner(), this::toggleForm);
        viewModel.getEditingTask().observe(getViewLifecycleOwner(), this::bindForm);
    }

    private void toggleForm(Boolean showing) {
        boolean formVisible = Boolean.TRUE.equals(showing);
        binding.listPane.setVisibility(formVisible ? View.GONE : View.VISIBLE);
        binding.formPane.setVisibility(formVisible ? View.VISIBLE : View.GONE);
        binding.fabAdd.setVisibility(formVisible ? View.GONE : View.VISIBLE);
        binding.emptyState.setVisibility(formVisible ? View.GONE : binding.emptyState.getVisibility());
        binding.headerTitle.setText(formVisible
                ? (viewModel.getEditingTask().getValue() == null ? getString(R.string.new_task) : getString(R.string.edit_task))
                : (viewModel.getFilter().getValue() != null ? viewModel.getFilter().getValue().title : getString(R.string.all_tasks)));
    }

    private void bindForm(Task task) {
        if (binding.formPane.getVisibility() != View.VISIBLE) {
            return;
        }
        refreshCategorySpinner();
        if (task == null) {
            binding.inputTitle.setText("");
            binding.inputDescription.setText("");
            binding.inputPriority.setText("Media", false);
            selectedDueDate = 0;
            binding.inputDueDate.setText("");
            if (!categories.isEmpty()) {
                binding.inputCategory.setText(categories.get(0).name, false);
            }
            return;
        }
        binding.inputTitle.setText(task.title);
        binding.inputDescription.setText(task.description);
        binding.inputPriority.setText(priorityLabel(task.priority), false);
        selectedDueDate = task.dueDate;
        binding.inputDueDate.setText(task.dueDate > 0
                ? DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(task.dueDate))
                : "");
        Category category = viewModel.findCategory(task.categoryId);
        if (category != null) {
            binding.inputCategory.setText(category.name, false);
        }
    }

    private void refreshCategorySpinner() {
        List<String> names = new ArrayList<>();
        for (Category category : categories) {
            names.add(category.name);
        }
        binding.inputCategory.setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                names
        ));
    }

    private void persistForm() {
        String title = String.valueOf(binding.inputTitle.getText()).trim();
        String description = String.valueOf(binding.inputDescription.getText()).trim();
        if (title.isEmpty()) {
            binding.inputTitle.setError(getString(R.string.title_required));
            return;
        }
        if (categories.isEmpty()) {
            Toast.makeText(requireContext(), R.string.no_categories, Toast.LENGTH_SHORT).show();
            return;
        }
        long categoryId = categories.get(0).id;
        String selectedName = String.valueOf(binding.inputCategory.getText());
        for (Category category : categories) {
            if (category.name.equals(selectedName)) {
                categoryId = category.id;
                break;
            }
        }
        viewModel.saveTask(title, description, categoryId, priorityValue(), selectedDueDate);
        Toast.makeText(requireContext(), R.string.task_saved, Toast.LENGTH_SHORT).show();
    }

    private int priorityValue() {
        String value = String.valueOf(binding.inputPriority.getText());
        if ("Alta".equals(value)) {
            return Task.PRIORITY_HIGH;
        }
        if ("Baja".equals(value)) {
            return Task.PRIORITY_LOW;
        }
        return Task.PRIORITY_MEDIUM;
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

    private void pickDueDate() {
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.due_date)
                .setSelection(selectedDueDate > 0 ? selectedDueDate : MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        picker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            selectedDueDate = calendar.getTimeInMillis();
            binding.inputDueDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(selectedDueDate)));
        });
        picker.show(getParentFragmentManager(), "dueDate");
    }

    private void confirmDelete(Task task) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_task)
                .setMessage(getString(R.string.delete_confirm, task.title))
                .setPositiveButton(R.string.delete, (dialog, which) -> viewModel.deleteTask(task))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
