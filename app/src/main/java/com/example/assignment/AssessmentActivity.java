package com.example.assignment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AssessmentActivity extends AppCompatActivity {

    private TextInputEditText weightEditText, heightEditText, bpEditText;
    private MaterialButton saveButton;
    private DatabaseHelper dbHelper;
    private List<Assessment> assessments ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        dbHelper = new DatabaseHelper(this);
        assessments = dbHelper.getAllAssessments();

        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        weightEditText = findViewById(R.id.weightEditText);
        heightEditText = findViewById(R.id.heightEditText);
        bpEditText = findViewById(R.id.bpEditText);
        saveButton = findViewById(R.id.saveButton);

        RecyclerView recyclerView = findViewById(R.id.assessmentsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AssessmentAdapter adapter = new AssessmentAdapter(assessments);
        recyclerView.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAssessment(adapter);
            }
        });
    }

    private void saveAssessment(AssessmentAdapter adapter) {
        String weight = weightEditText.getText().toString();
        String height = heightEditText.getText().toString();
        String bp = bpEditText.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_WEIGHT, weight);
        values.put(DatabaseHelper.COLUMN_HEIGHT, height);
        values.put(DatabaseHelper.COLUMN_BP, bp);
        values.put(DatabaseHelper.COLUMN_DATE, System.currentTimeMillis());

        long newRowId = db.insert(DatabaseHelper.TABLE_ASSESSMENTS, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Assessment saved successfully", Toast.LENGTH_SHORT).show();
            adapter.addAssessment(new Assessment(weight, height, bp, System.currentTimeMillis()));
            adapter.notifyItemInserted(assessments.size() - 1);
        } else {
            Toast.makeText(this, "Error saving assessment", Toast.LENGTH_SHORT).show();
        }
    }
}