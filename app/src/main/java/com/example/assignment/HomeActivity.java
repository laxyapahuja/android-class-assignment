package com.example.assignment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Response;
import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity implements SensorEventListener {

    private TextView stepCountTextView;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private int stepCount = 0;
//    RequestQueue queue;

    private BroadcastReceiver stepUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("STEP_UPDATE")) {
                int steps = intent.getIntExtra("steps", 0);
                updateStepCount(steps);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

//        queue = Volley.newRequestQueue(this);
//        StringRequest request = new StringRequest(
//                Request.Method.GET,
//                "https://8354-2401-4900-62f0-bca-f8ca-5b8e-67f8-c164.ngrok-free.app/add?a=10&b=20",
//                response -> {
//                    stepCountTextView.setText(response);
//                },
//                error -> {
//                    Log.d("HomeActivity", error.getMessage());
//                }
//        );
//
//        queue.add(request);

        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                    1);
        }
        stepCountTextView = findViewById(R.id.stepCountTextView);
        MaterialButton profileButton = findViewById(R.id.profileButton);
        MaterialButton assessmentButton = findViewById(R.id.assessmentButton);
        MaterialButton bmiButton = findViewById(R.id.bmiButton);

        profileButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));
        assessmentButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AssessmentActivity.class)));
        bmiButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, BMICalculatorActivity.class)));

        sendNotification("Great job!", "You've reached steps!");

        // Initialize step sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // Register BroadcastReceiver
        IntentFilter filter = new IntentFilter("STEP_UPDATE");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(stepUpdateReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(stepUpdateReceiver);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            updateStepCount(stepCount);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void updateStepCount(int steps) {
        stepCountTextView.setText("Steps: " + steps);
        if (steps % 1000 == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                sendNotification("Great job!", "You've reached " + steps + " steps!");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void sendNotification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "step_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null && notificationManager.getNotificationChannel("step_channel") == null) {
            NotificationChannel channel = new NotificationChannel(
                    "step_channel",
                    "Step Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notifications for step count milestones");
            notificationManager.createNotificationChannel(channel);
        }
    }
}