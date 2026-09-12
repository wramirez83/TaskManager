package co.edu.poli.taskmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import co.edu.poli.taskmanager.data.Task;
import co.edu.poli.taskmanager.databinding.ActivityMainBinding;
import co.edu.poli.taskmanager.ui.BrowserFragment;
import co.edu.poli.taskmanager.ui.CreateTaskFragment;
import co.edu.poli.taskmanager.ui.GalleryFragment;
import co.edu.poli.taskmanager.ui.HomeFragment;
import co.edu.poli.taskmanager.ui.StatsFragment;
import co.edu.poli.taskmanager.ui.TaskFilter;
import co.edu.poli.taskmanager.ui.TaskListFragment;
import co.edu.poli.taskmanager.ui.TaskViewModel;
import co.edu.poli.taskmanager.ui.VideoFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TaskViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.nav_open,
                R.string.nav_close
        );
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(item -> {
            navigate(item.getItemId());
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        if (savedInstanceState == null) {
            show(new HomeFragment(), getString(R.string.app_name));
            binding.navView.setCheckedItem(R.id.nav_home);
        }
    }

    public void openTasks(TaskFilter filter) {
        viewModel.selectFilter(filter);
        show(new TaskListFragment(), filter.title);
        binding.navView.setCheckedItem(R.id.nav_tasks);
    }

    public void openCreate() {
        viewModel.startCreate();
        show(new CreateTaskFragment(), getString(R.string.new_task));
        binding.navView.setCheckedItem(R.id.nav_create);
    }

    public void openEdit(Task task) {
        viewModel.startEdit(task);
        show(new CreateTaskFragment(), getString(R.string.edit_task));
        binding.navView.setCheckedItem(R.id.nav_create);
    }

    private void navigate(int itemId) {
        if (itemId == R.id.nav_home) {
            show(new HomeFragment(), getString(R.string.app_name));
        } else if (itemId == R.id.nav_tasks) {
            viewModel.selectFilter(TaskFilter.all());
            show(new TaskListFragment(), getString(R.string.task_list));
        } else if (itemId == R.id.nav_create) {
            openCreate();
        } else if (itemId == R.id.nav_stats) {
            show(new StatsFragment(), getString(R.string.stats));
        } else if (itemId == R.id.nav_gallery) {
            show(new GalleryFragment(), getString(R.string.gallery));
        } else if (itemId == R.id.nav_video) {
            show(new VideoFragment(), getString(R.string.video));
        } else if (itemId == R.id.nav_browser) {
            show(new BrowserFragment(), getString(R.string.browser));
        }
    }

    private void show(Fragment fragment, String title) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        binding.toolbar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            promptSearch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void promptSearch() {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint(R.string.search_hint);
        new AlertDialog.Builder(this)
                .setTitle(R.string.search)
                .setView(input)
                .setPositiveButton(R.string.search_action, (d, w) -> {
                    String query = input.getText().toString().trim();
                    if (query.isEmpty()) {
                        Toast.makeText(this, R.string.no_results, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    openTasks(TaskFilter.search(query));
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
