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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class MyFood extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    EditText search_edit_text;

    MyDatabaseHelper myDB;
    ArrayList<String> food_id, food_title, kcal, carbs, chat_beo, chat_dam, gram;
    CustomAdapter customAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myfood, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        add_button = view.findViewById(R.id.add_button);
        search_edit_text = view.findViewById(R.id.search_edit_text);

        add_button.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddActivity.class);
            startActivity(intent);
        });

        myDB = new MyDatabaseHelper(getActivity());
        food_id = new ArrayList<>();
        food_title = new ArrayList<>();
        kcal = new ArrayList<>();
        carbs = new ArrayList<>();
        chat_beo = new ArrayList<>();
        chat_dam = new ArrayList<>();
        gram = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter((FragmentActivity) getActivity(), getContext(), food_id, food_title, kcal, carbs, chat_beo, chat_dam, gram);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllFoodData();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                food_id.add(cursor.getString(0));
                food_title.add(cursor.getString(1));
                kcal.add(cursor.getString(2));
                carbs.add(cursor.getString(3));
                chat_beo.add(cursor.getString(4));
                chat_dam.add(cursor.getString(5));
                gram.add(cursor.getString(6));
            }
        }
    }

    void filter(String text) {
        ArrayList<String> filtered_food_id = new ArrayList<>();
        ArrayList<String> filtered_food_title = new ArrayList<>();
        ArrayList<String> filtered_kcal = new ArrayList<>();
        ArrayList<String> filtered_carbs = new ArrayList<>();
        ArrayList<String> filtered_chat_beo = new ArrayList<>();
        ArrayList<String> filtered_chat_dam = new ArrayList<>();
        ArrayList<String> filtered_gram = new ArrayList<>();

        for (int i = 0; i < food_title.size(); i++) {
            if (food_title.get(i).toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                filtered_food_id.add(food_id.get(i));
                filtered_food_title.add(food_title.get(i));
                filtered_kcal.add(kcal.get(i));
                filtered_carbs.add(carbs.get(i));
                filtered_chat_beo.add(chat_beo.get(i));
                filtered_chat_dam.add(chat_dam.get(i));
                filtered_gram.add(gram.get(i));
            }
        }

        customAdapter.updateData(filtered_food_id, filtered_food_title, filtered_kcal, filtered_carbs, filtered_chat_beo, filtered_chat_dam, filtered_gram);
    }
}
