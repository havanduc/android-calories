package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DangKyTK extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangkytk);

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        Button signUpButton = findViewById(R.id.btn_sign_up);
        Button backToLoginButton = findViewById(R.id.btn_back_to_login);

        signUpButton.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (!password.equals(confirmPassword)) {
                Toast.makeText(DangKyTK.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("Main", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(), "Registration successful: " + user.getEmail(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(DangKyTK.this, TestActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.w("Main", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(DangKyTK.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        backToLoginButton.setOnClickListener(view -> {
            Intent intent = new Intent(DangKyTK.this, login_app.class);
            startActivity(intent);
            finish();
        });
    }
}
