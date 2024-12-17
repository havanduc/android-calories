package com.example.myapplication.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class AddUser extends AppCompatActivity {
    Button addUserButton;
    EditText nameInput, ageInput, heightInput, weightInput, genderInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_user);


      nameInput = findViewById(R.id.name_input);
       ageInput = findViewById(R.id.age_input);
              heightInput = findViewById(R.id.height_input);
               weightInput = findViewById(R.id.weight_input);
              genderInput = findViewById(R.id.gender_input);
               addUserButton = findViewById(R.id.add_user_button);

               addUserButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                   public void onClick(View view) {
                       MyDatabaseHelper myDB = new MyDatabaseHelper(AddUser.this);
              myDB.addUser(
                        nameInput.getText().toString().trim(),
                       ageInput.getText().toString().trim(),
                        heightInput.getText().toString().trim(),
                        weightInput.getText().toString().trim(),
                      genderInput.getText().toString().trim()
               );
           }
       });
    }
}
