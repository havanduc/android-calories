package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PhysicalActivityAdapter extends RecyclerView.Adapter<PhysicalActivityAdapter.PhysicalActivityViewHolder> {
    private Context context;
    private ArrayList<Integer> activityIds;
    private ArrayList<String> activityNames;
    private ArrayList<Integer> durations;
    private ArrayList<Integer> caloriesBurned; // Chuyển từ Double thành Integer

    public PhysicalActivityAdapter(Context context, ArrayList<Integer> activityIds, ArrayList<String> activityNames, ArrayList<Integer> durations, ArrayList<Integer> caloriesBurned) {
        this.context = context;
        this.activityIds = activityIds;
        this.activityNames = activityNames;
        this.durations = durations;
        this.caloriesBurned = caloriesBurned;
    }

    @NonNull
    @Override
    public PhysicalActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_physical_activity, parent, false);
        return new PhysicalActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhysicalActivityViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.activityName.setText(activityNames.get(position));
        holder.duration.setText("Thời gian: " + durations.get(position) + " Phút");
        holder.caloriesBurned.setText("Lượng calo đốt cháy: " + caloriesBurned.get(position));

        // Set OnClickListener to edit the activity
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdatePhysicalActivityActivity.class);
                intent.putExtra("activityId", activityIds.get(position));
                intent.putExtra("activityName", activityNames.get(position));
                intent.putExtra("duration", String.valueOf(durations.get(position)));
                intent.putExtra("caloriesBurned", String.valueOf(caloriesBurned.get(position)));
                context.startActivity(intent);
            }
        });

        // Set OnLongClickListener to delete the activity
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                confirmDelete(activityIds.get(position), activityNames.get(position));
                return true;
            }
        });

        // Set OnClickListener for "Thêm" button
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userId = 1; // Thay đổi thành giá trị phù hợp với ứng dụng của bạn
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());

                // Lấy lượng calo từ thẻ hiển thị trên màn hình
                int caloriesBurnedValue = caloriesBurned.get(position);

                // Thêm vào bảng total_substances
                MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                myDB.addOrUpdateActivity(userId, caloriesBurnedValue);
                Log.d("DatabaseCheck", "Updated activity for userId: " + userId + ", caloriesBurned: " + caloriesBurnedValue);

                Toast.makeText(context, "Activity added to total substances", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDelete(int activityId, String activityName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete " + activityName + "?");
        builder.setMessage("Bạn có chắc chắn muốn xóa không? " + activityName + "?");
        builder.setPositiveButton("Có ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteActivity(activityId);
            }
        });
        builder.setNegativeButton("Không", null);
        builder.create().show();
    }

    private void deleteActivity(int activityId) {
        MyDatabaseHelper myDB = new MyDatabaseHelper(context);
        myDB.deletePhysicalActivity(activityId);

        // Refresh the data after deleting
        int index = activityIds.indexOf(activityId);
        activityIds.remove(index);
        activityNames.remove(index);
        durations.remove(index);
        caloriesBurned.remove(index);
        notifyDataSetChanged();

        Toast.makeText(context, "Xóa hoạt động thành công ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return activityIds.size();
    }

    public void updateData(ArrayList<Integer> new_activityIds, ArrayList<String> new_activityNames, ArrayList<Integer> new_durations, ArrayList<Integer> new_caloriesBurned) {
        activityIds = new_activityIds;
        activityNames = new_activityNames;
        durations = new_durations;
        caloriesBurned = new_caloriesBurned;
        notifyDataSetChanged();
    }

    public static class PhysicalActivityViewHolder extends RecyclerView.ViewHolder {
        TextView activityName, duration, caloriesBurned;
        CardView cardView;
        LinearLayout mainLayout;
        Button addButton;

        public PhysicalActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.activity_name);
            duration = itemView.findViewById(R.id.duration);
            caloriesBurned = itemView.findViewById(R.id.calories_burned);
            cardView = itemView.findViewById(R.id.cardView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            addButton = itemView.findViewById(R.id.add_button_physiscal); // Đảm bảo bạn đã khai báo nút này trong XML layout
        }
    }

}