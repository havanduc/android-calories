package com.example.myapplication.fragment;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class BMIAdapter extends RecyclerView.Adapter<BMIAdapter.BMIViewHolder> {
    private Context context;
    private Cursor cursor;

    public BMIAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public BMIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bmi, parent, false);
        return new BMIViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BMIViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            double bmiValue = cursor.getDouble(cursor.getColumnIndexOrThrow("bmi"));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));
            String suggestion = cursor.getString(cursor.getColumnIndexOrThrow("suggestion"));

            holder.bmiValue.setText(String.format("BMI: %.2f", bmiValue));
            holder.bmiTimestamp.setText("Timestamp: " + timestamp);
            holder.bmiSuggestion.setText(suggestion);
        }
    }

    @Override
    public int getItemCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    public static class BMIViewHolder extends RecyclerView.ViewHolder {
        TextView bmiValue, bmiTimestamp, bmiSuggestion;

        public BMIViewHolder(@NonNull View itemView) {
            super(itemView);
            bmiValue = itemView.findViewById(R.id.bmi_value);
            bmiTimestamp = itemView.findViewById(R.id.bmi_timestamp);
            bmiSuggestion = itemView.findViewById(R.id.bmi_suggestion);
        }
    }
}
