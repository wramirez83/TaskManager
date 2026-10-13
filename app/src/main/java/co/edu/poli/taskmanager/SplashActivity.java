package co.edu.poli.taskmanager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import co.edu.poli.taskmanager.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 1800L;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable openMain = this::openMain;
    private boolean launched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.logo.setAlpha(0f);
        binding.logo.setScaleX(0.84f);
        binding.logo.setScaleY(0.84f);
        binding.title.setAlpha(0f);
        binding.title.setTranslationY(18f);
        binding.progress.setScaleX(0f);

        binding.logo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(700)
                .setInterpolator(new OvershootInterpolator(1.15f))
                .start();
        binding.title.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(260)
                .setDuration(520)
                .setInterpolator(new DecelerateInterpolator())
                .start();
        binding.progress.post(() -> {
            binding.progress.setPivotX(binding.progress.getWidth() / 2f);
            binding.progress.animate()
                    .scaleX(1f)
                    .setDuration(SPLASH_DELAY_MS - 200)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        });

        handler.postDelayed(openMain, SPLASH_DELAY_MS);
    }

    private void openMain() {
        if (launched || isFinishing()) {
            return;
        }
        launched = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                    OVERRIDE_TRANSITION_OPEN,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(openMain);
        super.onDestroy();
    }
}
