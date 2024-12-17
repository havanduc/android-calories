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

public class UpdateUserActivity extends AppCompatActivity {

    EditText editUserName, editUserAge, editUserHeight, editUserWeight, editUserGender;
    Button saveButton, deleteButton;
    String userId;
    static final int REQUEST_DELETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);

        // Khởi tạo các thành phần giao diện
        editUserName = findViewById(R.id.edit_user_name);
        editUserAge = findViewById(R.id.edit_user_age);
        editUserHeight = findViewById(R.id.edit_user_height);
        editUserWeight = findViewById(R.id.edit_user_weight);
        editUserGender = findViewById(R.id.edit_user_gender);
        saveButton = findViewById(R.id.save_button1);
        deleteButton = findViewById(R.id.delete_button1);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("id");
        editUserName.setText(intent.getStringExtra("name"));
        editUserAge.setText(intent.getStringExtra("age"));
        editUserHeight.setText(intent.getStringExtra("height"));
        editUserWeight.setText(intent.getStringExtra("weight"));
        editUserGender.setText(intent.getStringExtra("gender"));

        // Xử lý sự kiện khi nhấn nút Lưu
        saveButton.setOnClickListener(v -> saveChanges());

        // Xử lý sự kiện khi nhấn nút Xóa
        deleteButton.setOnClickListener(v -> confirmDelete());
    }

    private void saveChanges() {
        String newName = editUserName.getText().toString();
        String newAge = editUserAge.getText().toString();
        String newHeight = editUserHeight.getText().toString();
        String newWeight = editUserWeight.getText().toString();
        String newGender = editUserGender.getText().toString();

        // Cập nhật cơ sở dữ liệu với thông tin mới
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);
        myDB.updateUser(userId, newName, newAge, newHeight, newWeight, newGender);

        Toast.makeText(this, "User updated", Toast.LENGTH_SHORT).show();

        // Gửi kết quả trả về
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", userId);
        resultIntent.putExtra("name", newName);
        resultIntent.putExtra("age", newAge);
        resultIntent.putExtra("height", newHeight);
        resultIntent.putExtra("weight", newWeight);
        resultIntent.putExtra("gender", newGender);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + editUserName.getText().toString() + "?");
        builder.setMessage("Are you sure you want to delete " + editUserName.getText().toString() + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteUser();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Hành động khi nhấn No
            }
        });
        builder.create().show();
    }

    private void deleteUser() {
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);
        myDB.deleteUser(userId);

        Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();

        // Gửi kết quả trả về
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", userId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
