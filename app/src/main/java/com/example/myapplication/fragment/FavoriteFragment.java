package com.example.myapplication.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FavoriteFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton add_button1;

    MyDatabaseHelper myDB;
    ArrayList<String> user_id, name, age, height, weight, gender;
    UserAdapter userAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.recyclerView1);
        add_button1 = view.findViewById(R.id.add_button1);
        add_button1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddUser.class);
            startActivity(intent);
        });

        myDB = new MyDatabaseHelper(getActivity());
        user_id = new ArrayList<>();
        name = new ArrayList<>();
        age = new ArrayList<>();
        height = new ArrayList<>();
        weight = new ArrayList<>();
        gender = new ArrayList<>();

        storeDataInArrays();

        userAdapter = new UserAdapter((FragmentActivity) getActivity(), getContext(), user_id, name, age, height, weight, gender);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllUserData();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                user_id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                age.add(cursor.getString(2));
                height.add(cursor.getString(3));
                weight.add(cursor.getString(4));
                gender.add(cursor.getString(5));
            }
        }
    }
}
