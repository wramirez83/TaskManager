package co.edu.poli.taskmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import co.edu.poli.taskmanager.R;
import co.edu.poli.taskmanager.databinding.FragmentBrowserBinding;
import co.edu.poli.taskmanager.databinding.ItemResourceBinding;

public class BrowserFragment extends Fragment {

    private FragmentBrowserBinding binding;

    private static final List<WebResource> RESOURCES = Arrays.asList(
            new WebResource("Técnica Pomodoro: Guía Definitiva", "Lectura de 5 minutos",
                    "https://es.wikipedia.org/wiki/T%C3%A9cnica_Pomodoro"),
            new WebResource("Cómo Evitar el Burnout", "Lectura de 8 minutos",
                    "https://es.wikipedia.org/wiki/S%C3%ADndrome_de_desgaste_profesional"),
            new WebResource("Plantilla de Objetivos OKRs", "Plantilla descargable",
                    "https://es.wikipedia.org/wiki/OKR")
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBrowserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ResourceAdapter adapter = new ResourceAdapter(RESOURCES, this::openUrl);
        binding.resourceList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.resourceList.setAdapter(adapter);
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.setWebChromeClient(new WebChromeClient());
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.buttonRefresh.setOnClickListener(v -> {
            if (binding.webView.getVisibility() == View.VISIBLE) {
                binding.webView.reload();
            } else {
                showResources();
            }
        });
        binding.urlBar.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                String url = binding.urlBar.getText().toString().trim();
                if (!url.startsWith("http")) {
                    url = "https://" + url;
                }
                openUrl(url);
                return true;
            }
            return false;
        });
        binding.buttonBack.setOnClickListener(v -> {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack();
            } else {
                showResources();
            }
        });
        binding.buttonForward.setOnClickListener(v -> {
            if (binding.webView.canGoForward()) {
                binding.webView.goForward();
            }
        });
        binding.buttonSharePage.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, binding.urlBar.getText().toString());
            startActivity(Intent.createChooser(share, getString(R.string.share)));
        });
        binding.buttonBookmark.setOnClickListener(v ->
                Toast.makeText(requireContext(), R.string.browser_bookmark, Toast.LENGTH_SHORT).show());
    }

    private void showResources() {
        binding.webView.setVisibility(View.GONE);
        binding.resourcesPane.setVisibility(View.VISIBLE);
        binding.urlBar.setText(R.string.browser_url);
    }

    private void openUrl(String url) {
        binding.resourcesPane.setVisibility(View.GONE);
        binding.webView.setVisibility(View.VISIBLE);
        binding.urlBar.setText(url.replace("https://", "").replace("http://", ""));
        binding.webView.loadUrl(url);
    }

    @Override
    public void onDestroyView() {
        if (binding != null) {
            binding.webView.destroy();
        }
        binding = null;
        super.onDestroyView();
    }

    static class WebResource {
        final String title;
        final String subtitle;
        final String url;

        WebResource(String title, String subtitle, String url) {
            this.title = title;
            this.subtitle = subtitle;
            this.url = url;
        }
    }

    static class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.Holder> {
        interface Listener {
            void onOpen(String url);
        }

        private final List<WebResource> items;
        private final Listener listener;

        ResourceAdapter(List<WebResource> items, Listener listener) {
            this.items = items;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(ItemResourceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            WebResource item = items.get(position);
            holder.binding.resourceTitle.setText(item.title);
            holder.binding.resourceSubtitle.setText(item.subtitle);
            holder.itemView.setOnClickListener(v -> listener.onOpen(item.url));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class Holder extends RecyclerView.ViewHolder {
            final ItemResourceBinding binding;

            Holder(ItemResourceBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
