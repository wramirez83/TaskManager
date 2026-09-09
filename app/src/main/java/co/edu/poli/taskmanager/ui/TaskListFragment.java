package co.edu.poli.taskmanager.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import co.edu.poli.taskmanager.MainActivity;
import co.edu.poli.taskmanager.R;
import co.edu.poli.taskmanager.data.Task;
import co.edu.poli.taskmanager.databinding.FragmentTaskListBinding;

public class TaskListFragment extends Fragment {

    private FragmentTaskListBinding binding;
    private TaskViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        TaskAdapter adapter = new TaskAdapter(new TaskAdapter.Listener() {
            @Override
            public void onToggle(Task task) {
                viewModel.toggleCompleted(task);
            }

            @Override
            public void onEdit(Task task) {
                ((MainActivity) requireActivity()).openEdit(task);
            }

            @Override
            public void onDelete(Task task) {
                new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.delete_task)
                        .setMessage(getString(R.string.delete_confirm, task.title))
                        .setPositiveButton(R.string.delete, (d, w) -> viewModel.deleteTask(task))
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        }, id -> viewModel.findCategory(id));
        binding.taskList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.taskList.setAdapter(adapter);
        viewModel.getVisibleTasks().observe(getViewLifecycleOwner(), tasks -> {
            adapter.submit(tasks);
            binding.emptyState.setVisibility(tasks == null || tasks.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
