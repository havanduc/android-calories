package com.example.myapplication.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class HistoryFragment extends Fragment {

    MyDatabaseHelper myDB;
    EditText dateEditText;
    TextView dateTextView, totalCaloriesTextView, totalCarbsTextView, totalFatTextView, totalProteinTextView, caloriesConsumedTextView;
    int userId = 1; // Đặt userId phù hợp

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        dateEditText = view.findViewById(R.id.dateEditText);
        dateTextView = view.findViewById(R.id.dateTextView);
        totalCaloriesTextView = view.findViewById(R.id.totalCaloriesTextView);
        totalCarbsTextView = view.findViewById(R.id.totalCarbsTextView);
        totalFatTextView = view.findViewById(R.id.totalFatTextView);
        totalProteinTextView = view.findViewById(R.id.totalProteinTextView);
        caloriesConsumedTextView = view.findViewById(R.id.totalCaloriesConsumedTextView);

        myDB = new MyDatabaseHelper(getActivity());

        dateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String date = s.toString().trim();
                if (!date.isEmpty()) {
                    displayTotalSubstancesForDate(date);
                }
            }
        });

        return view;
    }

    public void displayTotalSubstancesForDate(String date) {
        Cursor cursor = myDB.getTotalSubstancesForDate(userId, date);
        if (cursor.getCount() == 0) {
            totalCaloriesTextView.setText("Tổng lượng calo trong ngày: 0");
            totalCarbsTextView.setText("Tổng chất carbs trong ngày: 0");
            totalFatTextView.setText("Tổng chất béo trong ngày: 0");
            totalProteinTextView.setText("Tổng chất đạm trong ngày: 0");
            caloriesConsumedTextView.setText("Tổng lượng calo tiêu thụ: 0");
            dateTextView.setText("Ngày: N/A");
        } else {
            if (cursor.moveToFirst()) {
                int totalCaloriesIndex = cursor.getColumnIndex("total_calories");
                int totalCarbsIndex = cursor.getColumnIndex("total_carbs");
                int totalFatIndex = cursor.getColumnIndex("total_fat");
                int totalProteinIndex = cursor.getColumnIndex("total_protein");
                int caloriesConsumedIndex = cursor.getColumnIndex("calories_consumed");
                int dateIndex = cursor.getColumnIndex("date");

                // Kiểm tra và đảm bảo các cột tồn tại
                if (totalCaloriesIndex != -1 && totalCarbsIndex != -1 && totalFatIndex != -1 && totalProteinIndex != -1 && caloriesConsumedIndex != -1 && dateIndex != -1) {
                    int totalCalories = cursor.getInt(totalCaloriesIndex);
                    int totalCarbs = cursor.getInt(totalCarbsIndex);
                    int totalFat = cursor.getInt(totalFatIndex);
                    int totalProtein = cursor.getInt(totalProteinIndex);
                    int caloriesConsumed = cursor.getInt(caloriesConsumedIndex);
                    String dateString = cursor.getString(dateIndex);

                    totalCaloriesTextView.setText("Tổng lượng calo trong ngày: " + totalCalories);
                    totalCarbsTextView.setText("Tổng chất carbs trong ngày: " + totalCarbs);
                    totalFatTextView.setText("Tổng chất béo trong ngày: " + totalFat);
                    totalProteinTextView.setText("Tổng chất đạm trong ngày: " + totalProtein);
                    caloriesConsumedTextView.setText("Tổng lượng calo tiêu thụ: " + caloriesConsumed);
                    dateTextView.setText("Ngày: " + dateString);
                }
            }
        }
        cursor.close();
    }
}
