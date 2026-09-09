package co.edu.poli.taskmanager.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import co.edu.poli.taskmanager.R;
import co.edu.poli.taskmanager.databinding.FragmentVideoBinding;

public class VideoFragment extends Fragment {

    private static final String VIDEO_URL =
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4";

    private FragmentVideoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVideoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonPlay.setOnClickListener(v -> play());
        binding.buttonLike.setOnClickListener(v ->
                Toast.makeText(requireContext(), R.string.like, Toast.LENGTH_SHORT).show());
        binding.buttonShare.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, getString(R.string.video_title) + "\n" + VIDEO_URL);
            startActivity(Intent.createChooser(share, getString(R.string.share)));
        });
        binding.buttonSave.setOnClickListener(v ->
                Toast.makeText(requireContext(), R.string.save, Toast.LENGTH_SHORT).show());
        binding.nextVideo.setOnClickListener(v -> play());
    }

    private void play() {
        binding.buttonPlay.setVisibility(View.GONE);
        binding.videoView.setVisibility(View.VISIBLE);
        MediaController controller = new MediaController(requireContext());
        controller.setAnchorView(binding.videoView);
        binding.videoView.setMediaController(controller);
        binding.videoView.setVideoURI(Uri.parse(VIDEO_URL));
        binding.videoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (binding != null) {
            binding.videoView.pause();
        }
    }

    @Override
    public void onDestroyView() {
        if (binding != null) {
            binding.videoView.stopPlayback();
        }
        binding = null;
        super.onDestroyView();
    }
}
