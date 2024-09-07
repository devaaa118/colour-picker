package com.example.colour;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
public class SplashActivity extends AppCompatActivity {
    private TextView app, name;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        lottieAnimationView = findViewById(R.id.lottie_animation_view);
        app = findViewById(R.id.app);
        name = findViewById(R.id.name);

        // Set Lottie animation
        lottieAnimationView.setAnimation(R.raw.animation);
        lottieAnimationView.setRepeatCount(0); // Ensure animation doesn't loop
        lottieAnimationView.playAnimation(); // Start animation

        // Add listener for animation events
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                appanimation();
                nameanimation();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                navigateToMainActivity();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        // Check animation progress manually
        lottieAnimationView.addAnimatorUpdateListener(animator -> {
            if (lottieAnimationView.getProgress() == 1f) {
                navigateToMainActivity();
            }
        });
    }
    public void appanimation(){
        app.post(()->{
            int centerX = (getResources().getDisplayMetrics().widthPixels - app.getWidth()) / 2;

            ObjectAnimator animX = ObjectAnimator.ofFloat(app, "translationX", app.getTranslationX(), centerX);

            animX.setDuration(1000); // Duration in milliseconds
            animX.setInterpolator(new AccelerateDecelerateInterpolator());
            animX.start();

        });

    }
    public void nameanimation() {
        name.post(() -> {
            // Set the initial position of name to the right edge of the screen (off-screen)
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            name.setTranslationX(screenWidth);  // Move name to the right edge

            // Calculate the center position
            int centerX = (screenWidth - name.getWidth()) / 2;

            // Create an animation to move name from the right edge to the center
            ObjectAnimator animY = ObjectAnimator.ofFloat(name, "translationX", screenWidth, centerX);
            animY.setDuration(1000);
            animY.setInterpolator(new AccelerateDecelerateInterpolator());
            animY.start();
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}




