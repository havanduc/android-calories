package com.example.myapplication.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PersonFragment extends Fragment {

    RecyclerView recyclerView;
    BMIAdapter bmiAdapter;
    FloatingActionButton addBMIButton;
    Button viewDetailButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addBMIButton = view.findViewById(R.id.add_bmi_button);
        addBMIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateAndAddBMI();
            }
        });

        viewDetailButton = view.findViewById(R.id.view_detail_button);
        viewDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent để mở DetailActivity
                Intent intent = new Intent(getContext(), DetailActivity.class);
                startActivity(intent);
            }
        });

        loadDataAnalysis();

        return view;
    }

    private void loadDataAnalysis() {
        MyDatabaseHelper myDB = new MyDatabaseHelper(getContext());
        Cursor cursor = myDB.readAllDataAnalysis();

        // Thêm log để kiểm tra dữ liệu
        if (cursor != null && cursor.moveToFirst()) {
            do {
                double bmi = cursor.getDouble(cursor.getColumnIndexOrThrow("bmi"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));
                String suggestion = cursor.getString(cursor.getColumnIndexOrThrow("suggestion"));

                Log.d("PersonFragment", "BMI: " + bmi);
                Log.d("PersonFragment", "Timestamp: " + timestamp);
                Log.d("PersonFragment", "Suggestion: " + suggestion);
            } while (cursor.moveToNext());
        }

        bmiAdapter = new BMIAdapter(getContext(), cursor);
        recyclerView.setAdapter(bmiAdapter);
    }

    private void calculateAndAddBMI() {
        MyDatabaseHelper myDB = new MyDatabaseHelper(getContext());

        // Sử dụng phương thức calculateBMI của MyDatabaseHelper
        double bmi = myDB.calculateBMI(1); // ID người dùng giả định

        if (bmi != 0) {
            // Thêm chỉ số BMI vào bảng data_analysis
            myDB.AddBMI(bmi);

            // Lấy dữ liệu BMI mới nhất và hiển thị gợi ý
            Cursor cursor = myDB.getLatestBMIData();
            if (cursor.moveToFirst()) {
                double latestBMI = cursor.getDouble(cursor.getColumnIndexOrThrow("bmi"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("formatted_date"));
                String bmiSuggestion = myDB.getBMISuggestion(1); // ID người dùng giả định
                Toast.makeText(getContext(), "BMI được thêm thành công \nChỉ số BMI mới nhất:: " + latestBMI + "\nDate: " + date + "\n" + bmiSuggestion, Toast.LENGTH_LONG).show();
            }
            cursor.close();

            // Tải lại dữ liệu
            loadDataAnalysis();
        } else {
            Toast.makeText(getContext(), "Không tìm thấy dữ liệu người dùng hoặc dữ liệu không hợp lệ", Toast.LENGTH_LONG).show();
        }
    }
}
