package com.example.cypherguard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;

public class MainActivity extends BaseActivity {

    private MaterialRadioButton caesarMethod;
    private MaterialRadioButton vigenereMethod;
    private MaterialButton encryptButton;
    private MaterialButton decryptButton;
    private MaterialButton settingsButton;
    private MaterialButton helpButton;
    private MaterialButton aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        caesarMethod = findViewById(R.id.caesar_method);
        vigenereMethod = findViewById(R.id.vigenere_method);
        encryptButton = findViewById(R.id.encrypt_button);
        decryptButton = findViewById(R.id.decrypt_button);
        settingsButton = findViewById(R.id.settings_button);
        helpButton = findViewById(R.id.help_button);
        aboutButton = findViewById(R.id.about_button);

        // Set up click listeners
        encryptButton.setOnClickListener(v -> onEncryptClicked());
        decryptButton.setOnClickListener(v -> onDecryptClicked());
        settingsButton.setOnClickListener(v -> onSettingsClicked());
        helpButton.setOnClickListener(v -> onHelpClicked());
        aboutButton.setOnClickListener(v -> onAboutClicked());

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            int systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            v.setPadding(0, systemBarInsets, 0, 0);
            return WindowInsetsCompat.CONSUMED;
        });

//        // Set up logo in Toolbar
//        ImageView logo = findViewById(R.id.logo);
//        logo.setImageResource(R.drawable.ic_logo);

        // Apply font size after initializing views
        applyFontSize(getSharedPreferences("app_settings", MODE_PRIVATE).getInt("font_size", 0));
    }

    private void onEncryptClicked() {
        if (isCaesarMethodSelected()) {
            Intent intent = new Intent(this, CaesarEncryptionActivity.class);
            startActivity(intent);
        } else if (isVigenereMethodSelected()) {
            Intent intent = new Intent(this, VigenereEncryptionActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.select_encryption_method, Toast.LENGTH_SHORT).show();
        }
    }

    private void onDecryptClicked() {
        if (isCaesarMethodSelected()) {
            Intent intent = new Intent(this, CaesarDecryptionActivity.class);
            startActivity(intent);
        } else if (isVigenereMethodSelected()) {
            Intent intent = new Intent(this, VigenereDecryptionActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.select_dencryption_method, Toast.LENGTH_SHORT).show();
        }
    }

    private void onSettingsClicked() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void onHelpClicked() {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    private void onAboutClicked() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private boolean isCaesarMethodSelected() {
        return caesarMethod.isChecked();
    }

    private boolean isVigenereMethodSelected() {
        return vigenereMethod.isChecked();
    }
}
