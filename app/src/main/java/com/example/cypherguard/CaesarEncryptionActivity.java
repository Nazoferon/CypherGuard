package com.example.cypherguard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CaesarEncryptionActivity extends BaseActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private static final int CREATE_FILE_REQUEST_CODE = 2;

    private TextInputEditText inputText;
    private TextInputEditText shiftInput;
    private TextInputEditText outputText;
    private MaterialButton encryptButton;
    private MaterialButton copyButton;
    private MaterialButton shareButton;
    private MaterialButton clearButton;
    private MaterialButton backButton;
    private MaterialButton selectFileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caesar_encryption);

        inputText = findViewById(R.id.input_text);
        shiftInput = findViewById(R.id.shift);
        outputText = findViewById(R.id.output_text);
        encryptButton = findViewById(R.id.encrypt_button);
        copyButton = findViewById(R.id.copy_button);
        shareButton = findViewById(R.id.share_button);
        clearButton = findViewById(R.id.clear_button);
        backButton = findViewById(R.id.back_button);
        selectFileButton = findViewById(R.id.select_file_button);

        encryptButton.setOnClickListener(v -> encryptText());
        copyButton.setOnClickListener(v -> copyToClipboard());
        shareButton.setOnClickListener(v -> showShareOptions());
        clearButton.setOnClickListener(v -> clearFields());
        backButton.setOnClickListener(v -> goBack());
        selectFileButton.setOnClickListener(v -> selectFile());
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(selectedFileUri)));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                reader.close();
                String fileContent = stringBuilder.toString();
                if (containsCyrillic(fileContent)) {
                    Toast.makeText(this, R.string.cyrillic_not_supported, Toast.LENGTH_SHORT).show();
                } else {
                    inputText.setText(fileContent);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.file_read_error, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedUri = data.getData();
            String textToSave = outputText.getText().toString();
            saveTextToUri(selectedUri, textToSave);
        }
    }

    private void encryptText() {
        String text = inputText.getText().toString();
        String shiftStr = shiftInput.getText().toString();

        if (text.isEmpty() || shiftStr.isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        int shift = Integer.parseInt(shiftStr);
        String encryptedText = caesarCipher(text, shift);
        outputText.setText(encryptedText);
    }

    private String caesarCipher(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char base;
                int alphabetSize;
                if (Character.isUpperCase(character)) {
                    base = 'A';
                    alphabetSize = 26; // Количество букв в английском алфавите
                } else {
                    base = 'a';
                    alphabetSize = 26; // Количество букв в английском алфавите
                }
                result.append((char) ((character - base + shift) % alphabetSize + base));
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    private boolean containsCyrillic(String text) {
        for (char character : text.toCharArray()) {
            if (Character.UnicodeBlock.of(character) == Character.UnicodeBlock.CYRILLIC) {
                return true;
            }
        }
        return false;
    }

    private void copyToClipboard() {
        String textToCopy = outputText.getText().toString();
        if (!textToCopy.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Encrypted Text", textToCopy);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, R.string.text_copied, Toast.LENGTH_SHORT).show();
        }
    }

    private void showShareOptions() {
        String textToShare = outputText.getText().toString();
        if (!textToShare.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.share_options_title)
                    .setItems(R.array.share_options, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                shareTextAsText(textToShare);
                                break;
                            case 1:
                                shareTextAsFile(textToShare);
                                break;
                            case 2:
                                saveTextToDevice(textToShare);
                                break;
                        }
                    })
                    .show();
        }
    }

    private void shareTextAsText(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    private void shareTextAsFile(String text) {
        File file = saveEncryptedTextToFile(text);
        if (file != null) {
            Uri selectedUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_STREAM, selectedUri);
            sendIntent.setType("text/plain");
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        } else {
            Toast.makeText(this, R.string.file_save_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTextToDevice(String text) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "encrypted_text_Caesar.txt");
        startActivityForResult(intent, CREATE_FILE_REQUEST_CODE);
    }

    private void saveTextToUri(Uri uri, String text) {
        try (OutputStreamWriter osw = new OutputStreamWriter(getContentResolver().openOutputStream(uri))) {
            osw.write(text);
            Toast.makeText(this, R.string.file_saved_successfully, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.file_save_error, Toast.LENGTH_SHORT).show();
        }
    }

    private File saveEncryptedTextToFile(String text) {
        try {
            File file = new File(getExternalFilesDir(null), "encrypted_text_Caesar.txt");
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(text);
            osw.close();
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void clearFields() {
        inputText.setText("");
        shiftInput.setText("");
        outputText.setText("");
    }

    private void goBack() {
        finish();
    }
}