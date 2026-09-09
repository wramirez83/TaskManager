package co.edu.poli.taskmanager.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.edu.poli.taskmanager.data.Photo;
import co.edu.poli.taskmanager.databinding.FragmentGalleryBinding;
import co.edu.poli.taskmanager.databinding.ItemPhotoBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private TaskViewModel viewModel;
    private final ActivityResultLauncher<Intent> picker = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        try {
                            requireContext().getContentResolver().takePersistableUriPermission(
                                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );
                        } catch (SecurityException ignored) {
                            // The picker still grants a temporary read for this session.
                        }
                        viewModel.addPhoto(uri.toString());
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        PhotoAdapter adapter = new PhotoAdapter();
        binding.photoGrid.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        binding.photoGrid.setAdapter(adapter);
        viewModel.getPhotos().observe(getViewLifecycleOwner(), adapter::submit);
        binding.fabAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            picker.launch(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    static class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.Holder> {
        private final List<Photo> items = new ArrayList<>();

        void submit(List<Photo> photos) {
            items.clear();
            if (photos != null) {
                items.addAll(photos);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(ItemPhotoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            Photo photo = items.get(position);
            ImageView image = holder.binding.photoImage;
            if (photo.uri.startsWith("android.resource://")) {
                String name = photo.uri.substring(photo.uri.lastIndexOf('/') + 1);
                int res = image.getResources().getIdentifier(name, "drawable", image.getContext().getPackageName());
                if (res != 0) {
                    image.setImageResource(res);
                    return;
                }
            }
            image.setImageURI(Uri.parse(photo.uri));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class Holder extends RecyclerView.ViewHolder {
            final ItemPhotoBinding binding;

            Holder(ItemPhotoBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
