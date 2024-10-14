package com.example.cypherguard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ImageView instagramIcon = findViewById(R.id.instagram_icon);
        ImageView twitterIcon = findViewById(R.id.twitter_icon);
        ImageView githubIcon = findViewById(R.id.github_icon);
        MaterialButton backButton = findViewById(R.id.back_button);

        instagramIcon.setOnClickListener(v -> openUrl("https://www.instagram.com/oiitimict_akk"));
        twitterIcon.setOnClickListener(v -> openUrl("https://twitter.com/oiitimict_akk"));
        githubIcon.setOnClickListener(v -> openUrl("https://github.com/Nazoferon"));

        backButton.setOnClickListener(v -> finish());
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}