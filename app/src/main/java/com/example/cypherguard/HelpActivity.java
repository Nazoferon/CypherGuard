package com.example.cypherguard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class HelpActivity extends BaseActivity {

    private MaterialButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> goBack());

        TextView helpContentTextView = findViewById(R.id.help_content_textview);
        String helpContent = getString(R.string.help_content);
        helpContentTextView.setText(Html.fromHtml(helpContent, Html.FROM_HTML_MODE_COMPACT));
        helpContentTextView.setMovementMethod(LinkMovementMethod.getInstance());

        MaterialButton contactButton = findViewById(R.id.contact_button);
        contactButton.setOnClickListener(v -> showContactDialog());

        MaterialButton caesarMethodButton = findViewById(R.id.caesar_method_button);
        caesarMethodButton.setOnClickListener(v -> openCaesarMethodLink());

        MaterialButton vigenereMethodButton = findViewById(R.id.vigenere_method_button);
        vigenereMethodButton.setOnClickListener(v -> openVigenereMethodLink());
    }

    private void showContactDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.contact_dialog_title)
                .setMessage(R.string.contact_dialog_message + "\n\n" +
                        getString(R.string.contact_email) + "\n" +
                        getString(R.string.contact_phone))
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void openCaesarMethodLink() {
        String url = "https://uk.php.brj.cz/sifr-cezarya-yak-vin-pracyue";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void openVigenereMethodLink() {
        String url = "https://uk.wikipedia.org/wiki/Шифр_Віженера";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void goBack() {
        finish();
    }
}
