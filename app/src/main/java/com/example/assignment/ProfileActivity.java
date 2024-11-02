package com.example.assignment;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 100;
    private TextInputEditText nameEditText, phoneEditText, emergencyContactEditText;
    private MaterialButton saveButton, sendEmergencySMSButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emergencyContactEditText = findViewById(R.id.emergencyContactEditText);
        saveButton = findViewById(R.id.saveButton);
        sendEmergencySMSButton = findViewById(R.id.sendEmergencySMSButton);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        // Load saved profile data
        loadProfileData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        sendEmergencySMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSmsPermission()) {
                    sendEmergencySMS();
                } else {
                    requestSmsPermission();
                }
            }
        });
    }

    private void loadProfileData() {
        String name = sharedPreferences.getString("name", "");
        String phone = sharedPreferences.getString("phone", "");
        String emergencyContact = sharedPreferences.getString("emergencyContact", "");

        nameEditText.setText(name);
        phoneEditText.setText(phone);
        emergencyContactEditText.setText(emergencyContact);
    }

    private void saveProfile() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String emergencyContact = emergencyContactEditText.getText().toString().trim();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.putString("emergencyContact", emergencyContact);
        editor.apply();

        Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show();
    }

    private boolean checkSmsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
    }

    private void sendEmergencySMS() {
        String emergencyContact = emergencyContactEditText.getText().toString();
        String message = "Emergency: I need help!";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(emergencyContact, null, message, null, null);
            Toast.makeText(this, "Emergency SMS sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendEmergencySMS();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}