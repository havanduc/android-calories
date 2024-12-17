package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private Context context;
    private FragmentActivity activity;
    private ArrayList<String> user_id, user_name, user_age, user_height, user_weight, user_gender;

    // Constructor
    public UserAdapter(FragmentActivity activity, Context context, ArrayList<String> user_id, ArrayList<String> user_name, ArrayList<String> user_age, ArrayList<String> user_height, ArrayList<String> user_weight, ArrayList<String> user_gender) {
        this.activity = activity;
        this.context = context;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_age = user_age;
        this.user_height = user_height;
        this.user_weight = user_weight;
        this.user_gender = user_gender;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        // Set data into views
        holder.user_id_txt.setText(user_id.get(position));
        holder.user_name_txt.setText(user_name.get(position));
        holder.user_age_txt.setText(user_age.get(position));
        holder.user_height_txt.setText(user_height.get(position));
        holder.user_weight_txt.setText(user_weight.get(position));
        holder.user_gender_txt.setText(user_gender.get(position));

        // Handle click event
        holder.userCardView.setOnClickListener(view -> {
            Intent intent = new Intent(context, UpdateUserActivity.class);
            intent.putExtra("id", user_id.get(position));
            intent.putExtra("name", user_name.get(position));
            intent.putExtra("age", user_age.get(position));
            intent.putExtra("height", user_height.get(position));
            intent.putExtra("weight", user_weight.get(position));
            intent.putExtra("gender", user_gender.get(position));
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return user_id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView user_id_txt, user_name_txt, user_age_txt, user_height_txt, user_weight_txt, user_gender_txt;
        CardView userCardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            user_id_txt = itemView.findViewById(R.id.user_id_txt);
            user_name_txt = itemView.findViewById(R.id.user_name_txt);
            user_age_txt = itemView.findViewById(R.id.user_age_txt);
            user_height_txt = itemView.findViewById(R.id.user_height_txt);
            user_weight_txt = itemView.findViewById(R.id.user_weight_txt);
            user_gender_txt = itemView.findViewById(R.id.user_gender_txt);
            userCardView = itemView.findViewById(R.id.user_card_view);
        }
    }
}
