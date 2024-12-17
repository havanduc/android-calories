package com.example.myapplication.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class AddPhysicalActivityActivity extends AppCompatActivity {
    Button add_activity_button;
    EditText activity_name_input, duration_input, calories_burned_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_physical_activity);

        activity_name_input = findViewById(R.id.activity_name_input);
        duration_input = findViewById(R.id.duration_input);
        calories_burned_input = findViewById(R.id.calories_burned_input);
        add_activity_button = findViewById(R.id.add_activity_button);

        add_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String activityName = activity_name_input.getText().toString().trim();
                String duration = duration_input.getText().toString().trim();
                String caloriesBurned = calories_burned_input.getText().toString().trim();

                if (activityName.isEmpty() || duration.isEmpty() || caloriesBurned.isEmpty()) {
                    Toast.makeText(AddPhysicalActivityActivity.this, "Vui lòng nhập đủ thông tin ", Toast.LENGTH_SHORT).show();
                    return;
                }

                MyDatabaseHelper myDB = new MyDatabaseHelper(AddPhysicalActivityActivity.this);
                myDB.addPhysicalActivity(activityName, duration, caloriesBurned);
                Toast.makeText(AddPhysicalActivityActivity.this, "Hoạt động được thêm thành công", Toast.LENGTH_SHORT).show();

                // Quay lại trang trước đó
                finish();
            }
        });
    }
}
