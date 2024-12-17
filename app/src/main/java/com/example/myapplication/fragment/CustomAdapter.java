package com.example.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private static final String TAG = "CustomAdapter";
    private FragmentActivity activity;
    private Context context;
    private ArrayList<String> food_id, food_title, kcal, carbs, chat_beo, chat_dam, gram;
    private MyDatabaseHelper myDB;

    CustomAdapter(FragmentActivity activity, Context context, ArrayList<String> food_id, ArrayList<String> food_title, ArrayList<String> kcal, ArrayList<String> carbs, ArrayList<String> chat_beo, ArrayList<String> chat_dam, ArrayList<String> gram) {
        this.activity = activity;
        this.context = context;
        this.food_id = food_id;
        this.food_title = food_title;
        this.kcal = kcal;
        this.carbs = carbs;
        this.chat_beo = chat_beo;
        this.chat_dam = chat_dam;
        this.gram = gram;
        myDB = new MyDatabaseHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.food_title_txt.setText(String.valueOf(food_title.get(position)));
        holder.kcal_txt.setText(String.valueOf(kcal.get(position)));
        holder.carbs_txt.setText(String.valueOf(carbs.get(position)));
        holder.chat_beo_txt.setText(String.valueOf(chat_beo.get(position)));
        holder.chat_dam_txt.setText(String.valueOf(chat_dam.get(position)));
        holder.gram_txt.setText(String.valueOf(gram.get(position)));

        // Xử lý sự kiện nhấn nút "Add Food"
        holder.add_food_button.setOnClickListener(v -> {
            int userId = 1; // Thay đổi thành giá trị phù hợp với ứng dụng của bạn
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());

            int calories = Integer.parseInt(kcal.get(position));
            int carbsValue = Integer.parseInt(carbs.get(position));
            int fat = Integer.parseInt(chat_beo.get(position));
            int protein = Integer.parseInt(chat_dam.get(position));

            // Thêm vào bảng total_substances mà không thêm lượng calo tiêu thụ
            myDB.addOrUpdateTotalSubstances(userId, calories, carbsValue, fat, protein, 0);

            Toast.makeText(context, "Thực phẩm đã được thêm vào quản lý chất ", Toast.LENGTH_SHORT).show();
        });

        // Set OnClickListener to edit the food item
        holder.cardView.setOnClickListener(v -> {
            Log.d(TAG, "CardView clicked: position=" + position + ", food_id=" + food_id.get(position));
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("id", food_id.get(position));
            intent.putExtra("title", food_title.get(position));
            intent.putExtra("kcal", kcal.get(position));
            intent.putExtra("carbs", carbs.get(position));
            intent.putExtra("chat_beo", chat_beo.get(position));
            intent.putExtra("chat_dam", chat_dam.get(position));
            intent.putExtra("gram", gram.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return food_id.size();
    }

    public void updateData(ArrayList<String> new_food_id, ArrayList<String> new_food_title, ArrayList<String> new_kcal, ArrayList<String> new_carbs, ArrayList<String> new_chat_beo, ArrayList<String> new_chat_dam, ArrayList<String> new_gram) {
        food_id = new_food_id;
        food_title = new_food_title;
        kcal = new_kcal;
        carbs = new_carbs;
        chat_beo = new_chat_beo;
        chat_dam = new_chat_dam;
        gram = new_gram;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView food_title_txt, kcal_txt, carbs_txt, chat_beo_txt, chat_dam_txt, gram_txt;
        Button add_food_button;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            food_title_txt = itemView.findViewById(R.id.food_title_txt);
            kcal_txt = itemView.findViewById(R.id.kcal_txt);
            carbs_txt = itemView.findViewById(R.id.carbs_txt);
            chat_beo_txt = itemView.findViewById(R.id.chat_beo_txt);
            chat_dam_txt = itemView.findViewById(R.id.chat_dam_txt);
            gram_txt = itemView.findViewById(R.id.gram_txt);
            add_food_button = itemView.findViewById(R.id.add_food_button);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
