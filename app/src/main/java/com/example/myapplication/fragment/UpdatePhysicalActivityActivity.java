package com.example.myapplication.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class UpdatePhysicalActivityActivity extends AppCompatActivity {

    EditText editTextActivityName, editTextDuration, editTextCaloriesBurned;
    Button buttonUpdateActivity, buttonDeleteActivity;
    int activityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_physical_activity);

        editTextActivityName = findViewById(R.id.edit_text_activity_name);
        editTextDuration = findViewById(R.id.edit_text_duration);
        editTextCaloriesBurned = findViewById(R.id.edit_text_calories_burned);
        buttonUpdateActivity = findViewById(R.id.button_update_activity);
        buttonDeleteActivity = findViewById(R.id.button_delete_activity);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        activityId = intent.getIntExtra("activityId", -1);
        editTextActivityName.setText(intent.getStringExtra("activityName"));
        editTextDuration.setText(intent.getStringExtra("duration"));
        editTextCaloriesBurned.setText(intent.getStringExtra("caloriesBurned"));

        // Xử lý sự kiện khi nhấn nút Lưu
        buttonUpdateActivity.setOnClickListener(v -> updatePhysicalActivity());

        // Xử lý sự kiện khi nhấn nút Xóa
        buttonDeleteActivity.setOnClickListener(v -> confirmDelete());
    }

    private void updatePhysicalActivity() {
        String newActivityName = editTextActivityName.getText().toString();
        String newDuration = editTextDuration.getText().toString();
        String newCaloriesBurned = editTextCaloriesBurned.getText().toString();

        // Kiểm tra đầu vào
        if (newActivityName.isEmpty() || newDuration.isEmpty() || newCaloriesBurned.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int duration = Integer.parseInt(newDuration);
        double caloriesBurned = Double.parseDouble(newCaloriesBurned);

        // Cập nhật cơ sở dữ liệu với thông tin mới
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);
        myDB.updatePhysicalActivity(activityId, newActivityName, duration, caloriesBurned);

        Toast.makeText(this, "Activity updated", Toast.LENGTH_SHORT).show();

        // Gửi kết quả trả về
        Intent resultIntent = new Intent();
        resultIntent.putExtra("activityId", activityId);
        resultIntent.putExtra("activityName", newActivityName);
        resultIntent.putExtra("duration", newDuration);
        resultIntent.putExtra("caloriesBurned", newCaloriesBurned);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + editTextActivityName.getText().toString() + "?");
        builder.setMessage("Are you sure you want to delete " + editTextActivityName.getText().toString() + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePhysicalActivity();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    private void deletePhysicalActivity() {
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);
        myDB.deletePhysicalActivity(activityId);

        Toast.makeText(this, "Activity deleted", Toast.LENGTH_SHORT).show();

        // Gửi kết quả trả về
        Intent resultIntent = new Intent();
        resultIntent.putExtra("activityId", activityId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
