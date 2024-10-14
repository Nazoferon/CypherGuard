package com.example.cypherguard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Запуск анімації
        ImageView logo = findViewById(R.id.logo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        logo.startAnimation(animation);

        // Затримка перед переходом на головну активність
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000); // 3 секунди
    }
}
