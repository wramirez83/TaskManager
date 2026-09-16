package co.edu.poli.taskmanager.ui;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import co.edu.poli.taskmanager.R;
import co.edu.poli.taskmanager.databinding.FragmentStatsBinding;
import co.edu.poli.taskmanager.databinding.ItemStatBinding;

public class StatsFragment extends Fragment {

    private FragmentStatsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TaskViewModel viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        viewModel.getStats().observe(getViewLifecycleOwner(), stats -> {
            if (stats == null || binding == null) {
                return;
            }
            binding.donut.setProgress(stats.completedToday, Math.max(stats.total, 1));
            binding.percentChip.setText(getString(R.string.percent_chip, stats.percent()));
            binding.completedRatio.setText(getString(
                    R.string.completed_ratio, stats.completedToday, stats.total));
            binding.completedPercent.setText(getString(R.string.completed_percent, stats.percent()));
            bindStat(binding.statHigh, R.string.high, stats.high, R.color.priority_high, R.string.stat_high_caption);
            bindStat(binding.statNormal, R.string.normal, stats.medium, R.color.stat_normal, R.string.stat_normal_caption);
            bindStat(binding.statLow, R.string.low, stats.low, R.color.priority_low, R.string.stat_low_caption);
            bindStat(binding.statOverdue, R.string.overdue, stats.overdue, R.color.priority_overdue, R.string.stat_overdue_caption);
        });
    }

    private void bindStat(ItemStatBinding card, int label, int value, int colorRes, int caption) {
        card.statLabel.setText(label);
        card.statValue.setText(String.valueOf(value));
        card.statCaption.setText(caption);
        GradientDrawable dot = (GradientDrawable) card.statDot.getBackground().mutate();
        dot.setColor(ContextCompat.getColor(requireContext(), colorRes));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
