package co.edu.poli.taskmanager;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import co.edu.poli.taskmanager.data.Task;
import co.edu.poli.taskmanager.databinding.ActivityMainBinding;
import co.edu.poli.taskmanager.databinding.BottomTabBarBinding;
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

    private static final String TAG_HOME = "home";
    private static final String TAG_TASKS = "tasks";
    private static final String TAG_CREATE = "create";
    private static final String TAG_STATS = "stats";
    private static final String TAG_GALLERY = "gallery";
    private static final String TAG_VIDEO = "video";
    private static final String TAG_BROWSER = "browser";

    private ActivityMainBinding binding;
    private TaskViewModel viewModel;
    private ActionBarDrawerToggle toggle;
    private int selectedTabId = R.id.tab_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        toggle = new ActionBarDrawerToggle(
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
            navigateDrawer(item.getItemId());
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        BottomTabBarBinding tabs = binding.bottomBar;
        tabs.tabHome.setOnClickListener(v -> openHome());
        tabs.tabTasks.setOnClickListener(v -> openTasks(TaskFilter.all()));
        tabs.tabCreate.setOnClickListener(v -> openCreate());
        tabs.tabStats.setOnClickListener(v -> openStats());

        getSupportFragmentManager().addOnBackStackChangedListener(this::syncSelectionFromVisibleFragment);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    return;
                }
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    return;
                }
                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        if (savedInstanceState == null) {
            openHome();
        } else {
            syncSelectionFromVisibleFragment();
        }
    }

    public void openHome() {
        showTab(new HomeFragment(), TAG_HOME, getString(R.string.app_name), R.id.nav_home, R.id.tab_home);
    }

    public void openTasks(TaskFilter filter) {
        viewModel.selectFilter(filter);
        showTab(new TaskListFragment(), TAG_TASKS, getString(R.string.task_list), R.id.nav_tasks, R.id.tab_tasks);
    }

    public void openCreate() {
        viewModel.startCreate();
        showTab(new CreateTaskFragment(), TAG_CREATE, getString(R.string.new_task), R.id.nav_create, R.id.tab_create);
    }

    public void openEdit(Task task) {
        viewModel.startEdit(task);
        showTab(new CreateTaskFragment(), TAG_CREATE, getString(R.string.edit_task), R.id.nav_create, R.id.tab_create);
    }

    public void openStats() {
        showTab(new StatsFragment(), TAG_STATS, getString(R.string.stats), R.id.nav_stats, R.id.tab_stats);
    }

    private void navigateDrawer(int itemId) {
        if (itemId == R.id.nav_home) {
            openHome();
        } else if (itemId == R.id.nav_tasks) {
            openTasks(TaskFilter.all());
        } else if (itemId == R.id.nav_create) {
            openCreate();
        } else if (itemId == R.id.nav_stats) {
            openStats();
        } else if (itemId == R.id.nav_gallery) {
            showExtra(new GalleryFragment(), TAG_GALLERY, getString(R.string.gallery), R.id.nav_gallery);
        } else if (itemId == R.id.nav_video) {
            showExtra(new VideoFragment(), TAG_VIDEO, getString(R.string.video), R.id.nav_video);
        } else if (itemId == R.id.nav_browser) {
            showExtra(new BrowserFragment(), TAG_BROWSER, getString(R.string.browser), R.id.nav_browser);
        }
    }

    private void showTab(Fragment fragment, String tag, String title, int drawerId, int tabId) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
        applySelection(title, drawerId, tabId);
    }

    private void showExtra(Fragment fragment, String tag, String title, int drawerId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(tag)
                .commit();
        applySelection(title, drawerId, 0);
    }

    private void applySelection(String title, int drawerId, int tabId) {
        binding.toolbar.setTitle(title);
        if (drawerId != 0) {
            binding.navView.setCheckedItem(drawerId);
        }
        selectedTabId = tabId;
        highlightTab(tabId);
    }

    private void syncSelectionFromVisibleFragment() {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (current instanceof HomeFragment) {
            applySelection(getString(R.string.app_name), R.id.nav_home, R.id.tab_home);
        } else if (current instanceof TaskListFragment) {
            applySelection(getString(R.string.task_list), R.id.nav_tasks, R.id.tab_tasks);
        } else if (current instanceof CreateTaskFragment) {
            String title = viewModel.getEditingTask().getValue() == null
                    ? getString(R.string.new_task)
                    : getString(R.string.edit_task);
            applySelection(title, R.id.nav_create, R.id.tab_create);
        } else if (current instanceof StatsFragment) {
            applySelection(getString(R.string.stats), R.id.nav_stats, R.id.tab_stats);
        } else if (current instanceof GalleryFragment) {
            applySelection(getString(R.string.gallery), R.id.nav_gallery, 0);
        } else if (current instanceof VideoFragment) {
            applySelection(getString(R.string.video), R.id.nav_video, 0);
        } else if (current instanceof BrowserFragment) {
            applySelection(getString(R.string.browser), R.id.nav_browser, 0);
        }
    }

    private void highlightTab(int tabId) {
        BottomTabBarBinding tabs = binding.bottomBar;
        styleTab(tabs.tabHomeIcon, tabs.tabHomeLabel, tabId == R.id.tab_home);
        styleTab(tabs.tabTasksIcon, tabs.tabTasksLabel, tabId == R.id.tab_tasks);
        styleTab(null, tabs.tabCreateLabel, tabId == R.id.tab_create);
        styleTab(tabs.tabStatsIcon, tabs.tabStatsLabel, tabId == R.id.tab_stats);
    }

    private void styleTab(ImageView icon, TextView label, boolean selected) {
        int color = ContextCompat.getColor(this, selected ? R.color.primary : R.color.nav_inactive);
        if (icon != null) {
            icon.setColorFilter(color);
        }
        label.setTextColor(color);
        label.setTypeface(null, selected ? Typeface.BOLD : Typeface.NORMAL);
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
        if (item.getItemId() == R.id.action_filter) {
            promptFilter();
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

    private void promptFilter() {
        String[] labels = {
                getString(R.string.filter_all),
                getString(R.string.filter_today),
                getString(R.string.filter_important),
                getString(R.string.filter_completed)
        };
        new AlertDialog.Builder(this)
                .setTitle(R.string.filter)
                .setItems(labels, (d, which) -> {
                    if (which == 1) {
                        openTasks(TaskFilter.today());
                    } else if (which == 2) {
                        openTasks(TaskFilter.important());
                    } else if (which == 3) {
                        openTasks(TaskFilter.completed());
                    } else {
                        openTasks(TaskFilter.all());
                    }
                })
                .show();
    }
}
