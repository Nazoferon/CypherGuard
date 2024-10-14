package com.example.cypherguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private BroadcastReceiver fontSizeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            applyFontSize(getSharedPreferences("app_settings", MODE_PRIVATE).getInt("font_size", 0));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(fontSizeReceiver, new IntentFilter("com.example.cypherguard.FONT_SIZE_CHANGED"), Context.RECEIVER_NOT_EXPORTED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyFontSize(getSharedPreferences("app_settings", MODE_PRIVATE).getInt("font_size", 0));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(fontSizeReceiver);
    }

    protected void applyFontSize(int fontSize) {
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
}
