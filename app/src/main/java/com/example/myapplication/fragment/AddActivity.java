package com.example.myapplication.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class AddActivity extends AppCompatActivity {
    Button add_button;
    EditText title_input, kcal_input, carbs_input, chat_beo_input, chat_dam_input, gram_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input);
        kcal_input = findViewById(R.id.kcal_input);
        carbs_input = findViewById(R.id.carbs_input);
        chat_beo_input = findViewById(R.id.chat_beo_input);
        chat_dam_input = findViewById(R.id.chat_dam_input);
        gram_input = findViewById(R.id.gram_input);
        add_button = findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addFood(
                        title_input.getText().toString().trim(),
                        kcal_input.getText().toString().trim(),
                        carbs_input.getText().toString().trim(),
                        chat_beo_input.getText().toString().trim(),
                        chat_dam_input.getText().toString().trim(),
                        gram_input.getText().toString().trim());
                Toast.makeText(AddActivity.this, "Thực phẩm được thêm vào thành công", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }
}
