package com.example.cypherguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "app_settings";
    private static final String KEY_FONT_SIZE = "font_size";

    private Spinner fontSizeSpinner;
    private MaterialButton saveButton;
    private MaterialButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        fontSizeSpinner = findViewById(R.id.font_size_spinner);
        saveButton = findViewById(R.id.save_button);
        backButton = findViewById(R.id.back_button);

        // Set up spinners
        ArrayAdapter<CharSequence> fontSizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.font_size_options, android.R.layout.simple_spinner_item);
        fontSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSizeSpinner.setAdapter(fontSizeAdapter);

        // Load saved settings
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int fontSize = preferences.getInt(KEY_FONT_SIZE, 0);

        fontSizeSpinner.setSelection(fontSize);

        // Set up save and back button
        saveButton.setOnClickListener(v -> saveSettings());
        backButton.setOnClickListener(v -> goBack());

        // Apply saved settings
        applySettings(fontSize);
    }

    private void saveSettings() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        int fontSize = fontSizeSpinner.getSelectedItemPosition();

        editor.putInt(KEY_FONT_SIZE, fontSize);
        editor.apply();

        // Apply settings
        applySettings(fontSize);

        // Notify other activities to apply the font size
        Intent intent = new Intent("com.example.cypherguard.FONT_SIZE_CHANGED");
        sendBroadcast(intent);

        finish();
    }

    private void applySettings(int fontSize) {
        // Apply font size
        applyFontSize(fontSize);
    }

    private void applyFontSize(int fontSize) {
        float textSize;
        switch (fontSize) {
            case 0:
                textSize = 14; // Small
                break;
            case 1:
                textSize = 18; // Medium
                break;
            case 2:
                textSize = 22; // Large
                break;
            default:
                textSize = 18; // Default to Medium
                break;
        }

        // Apply the text size to all TextViews in the activity
        applyTextSizeToAllTextViews(textSize);
    }

    private void applyTextSizeToAllTextViews(float textSize) {
        // Iterate through all views in the activity and apply the text size to TextViews
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        for (int i = 0; i < decorView.getChildCount(); i++) {
            View child = decorView.getChildAt(i);
            if (child instanceof ViewGroup) {
                applyTextSizeToViewGroup((ViewGroup) child, textSize);
            }
        }
    }

    private void applyTextSizeToViewGroup(ViewGroup viewGroup, float textSize) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextSize(textSize);
            } else if (child instanceof ViewGroup) {
                applyTextSizeToViewGroup((ViewGroup) child, textSize);
            }
        }
    }

    private void goBack() {
        finish();
    }
}
