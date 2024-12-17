package com.example.myapplication.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class DetailActivity extends AppCompatActivity {
    private MyDatabaseHelper myDB;
    private EditText editTextBmiId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Tìm kiếm EditText
        editTextBmiId = findViewById(R.id.edit_text_bmi_id);

        // Khởi tạo cơ sở dữ liệu
        myDB = new MyDatabaseHelper(this);

        // Xử lý nút xóa
        Button deleteButton = findViewById(R.id.delete_button1);
        deleteButton.setOnClickListener(v -> confirmDelete());

        // Nút quay lại
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa BMI?");
        builder.setMessage("Bạn có chắc chắn muốn xóa mục BMI này không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBMI();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Hành động khi nhấn No
            }
        });
        builder.create().show();
    }

    private void deleteBMI() {
        String bmiId = editTextBmiId.getText().toString();
        if (!bmiId.isEmpty()) {
            myDB.deleteBMI(bmiId);
            Toast.makeText(this, "BMI đã bị xóa", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Vui lòng nhập ID BMI hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}
