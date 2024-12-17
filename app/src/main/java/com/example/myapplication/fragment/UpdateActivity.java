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

public class UpdateActivity extends AppCompatActivity {

    EditText editFoodTitle, editKcal, editCarbs, editFat, editProtein, editGram;
    Button saveButton, deleteButton;
    String foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Khởi tạo các thành phần giao diện
        editFoodTitle = findViewById(R.id.edit_food_title);
        editKcal = findViewById(R.id.edit_kcal);
        editCarbs = findViewById(R.id.edit_carbs);
        editFat = findViewById(R.id.edit_fat);
        editProtein = findViewById(R.id.edit_protein);
        editGram = findViewById(R.id.edit_gram);
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_button);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        foodId = intent.getStringExtra("id");
        editFoodTitle.setText(intent.getStringExtra("title"));
        editKcal.setText(intent.getStringExtra("kcal"));
        editCarbs.setText(intent.getStringExtra("carbs"));
        editFat.setText(intent.getStringExtra("chat_beo"));
        editProtein.setText(intent.getStringExtra("chat_dam"));
        editGram.setText(intent.getStringExtra("gram"));

        // Xử lý sự kiện khi nhấn nút Lưu
        saveButton.setOnClickListener(v -> saveChanges());

        // Xử lý sự kiện khi nhấn nút Xóa
        deleteButton.setOnClickListener(v -> confirmDelete());
    }

    private void saveChanges() {
        String newTitle = editFoodTitle.getText().toString();
        String newKcal = editKcal.getText().toString();
        String newCarbs = editCarbs.getText().toString();
        String newFat = editFat.getText().toString();
        String newProtein = editProtein.getText().toString();
        String newGram = editGram.getText().toString();

        // Cập nhật cơ sở dữ liệu với thông tin mới
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);
        myDB.updateFood(foodId, newTitle, newKcal, newCarbs, newFat, newProtein, newGram, 1); // Thiết lập mặc định là 1

        Toast.makeText(this, "Food updated", Toast.LENGTH_SHORT).show();

        // Gửi kết quả trả về
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", foodId);
        resultIntent.putExtra("title", newTitle);
        resultIntent.putExtra("kcal", newKcal);
        resultIntent.putExtra("carbs", newCarbs);
        resultIntent.putExtra("chat_beo", newFat);
        resultIntent.putExtra("chat_dam", newProtein);
        resultIntent.putExtra("gram", newGram);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + editFoodTitle.getText().toString() + "?");
        builder.setMessage("Bạn có chắc chắn muốn xóa không? " + editFoodTitle.getText().toString() + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteFood();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Hành động khi nhấn No
            }
        });
        builder.create().show();
    }

    private void deleteFood() {
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);
        myDB.deleteOneRow(foodId);

        Toast.makeText(this, "Food deleted", Toast.LENGTH_SHORT).show();

        // Gửi kết quả trả về
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", foodId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
