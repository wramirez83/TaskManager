package co.edu.poli.taskmanager;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import co.edu.poli.taskmanager.ui.ContentFragment;
import co.edu.poli.taskmanager.ui.MenuFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.menu_container, new MenuFragment())
                    .replace(R.id.content_container, new ContentFragment())
                    .commit();
        }
    }
}
