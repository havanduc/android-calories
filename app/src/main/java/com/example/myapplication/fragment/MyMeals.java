package com.example.myapplication.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class MyMeals extends Fragment {

    RecyclerView recyclerView;
    PhysicalActivityAdapter activityAdapter;
    FloatingActionButton addActivityButton;
    EditText search_edit_text;

    MyDatabaseHelper myDB;
    ArrayList<Integer> activityIds;
    ArrayList<String> activityNames;
    ArrayList<Integer> durations;
    ArrayList<Integer> caloriesBurned; // Chuyển từ Double thành Integer

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mymeals, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPhysicalActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        search_edit_text = view.findViewById(R.id.search_edit_text);

        myDB = new MyDatabaseHelper(getContext());
        activityIds = new ArrayList<>();
        activityNames = new ArrayList<>();
        durations = new ArrayList<>();
        caloriesBurned = new ArrayList<>();

        addActivityButton = view.findViewById(R.id.add_activity_button);
        addActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddPhysicalActivityActivity.class);
                startActivity(intent);
            }
        });

        storeDataInArrays();

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void storeDataInArrays() {
        // Xóa dữ liệu cũ trong các ArrayList để tránh trùng lặp
        activityIds.clear();
        activityNames.clear();
        durations.clear();
        caloriesBurned.clear();

        // Đọc dữ liệu từ cơ sở dữ liệu
        Cursor cursor = myDB.readAllPhysicalActivitiesByUser(1); // ID người dùng giả định
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                int activityId = cursor.getInt(cursor.getColumnIndexOrThrow("activity_id"));
                String activityName = cursor.getString(cursor.getColumnIndexOrThrow("activity_name"));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                int caloriesBurnedValue = cursor.getInt(cursor.getColumnIndexOrThrow("calories_burned")); // Chuyển đổi sang Integer

                activityIds.add(activityId);
                activityNames.add(activityName);
                durations.add(duration);
                caloriesBurned.add(caloriesBurnedValue);
            }
        }
        cursor.close();

        // Cập nhật adapter và recyclerView
        activityAdapter = new PhysicalActivityAdapter(getContext(), activityIds, activityNames, durations, caloriesBurned);
        recyclerView.setAdapter(activityAdapter);
    }


    private void filter(String text) {
        ArrayList<Integer> filtered_activityIds = new ArrayList<>();
        ArrayList<String> filtered_activityNames = new ArrayList<>();
        ArrayList<Integer> filtered_durations = new ArrayList<>();
        ArrayList<Integer> filtered_caloriesBurned = new ArrayList<>(); // Chuyển từ Double thành Integer

        for (int i = 0; i < activityNames.size(); i++) {
            if (activityNames.get(i).toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                filtered_activityIds.add(activityIds.get(i));
                filtered_activityNames.add(activityNames.get(i));
                filtered_durations.add(durations.get(i));
                filtered_caloriesBurned.add(caloriesBurned.get(i));
            }
        }

        activityAdapter.updateData(filtered_activityIds, filtered_activityNames, filtered_durations, filtered_caloriesBurned);
    }
}
