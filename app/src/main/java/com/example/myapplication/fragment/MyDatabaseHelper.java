package com.example.myapplication.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "CALORIES.db";
    private static final int DATABASE_VERSION = 11;


    // Bảng my_food
    private static final String TABLE_FOOD = "my_food";
    private static final String COLUMN_FOOD_ID = "food_id";
    private static final String COLUMN_FOOD_TITLE = "food_title";
    private static final String COLUMN_FOOD_KCAL = "kcal";
    private static final String COLUMN_FOOD_CARBS = "carbs";
    private static final String COLUMN_FOOD_CHATBEO = "chat_beo";
    private static final String COLUMN_FOOD_CHATDAM = "chat_dam";
    private static final String COLUMN_FOOD_KHOILUONG = "gram";
    private static final String COLUMN_USER_ID_FK = "user_id_fk";

    // Bảng my_user
    private static final String TABLE_USER = "my_user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_AGE = "user_age";
    private static final String COLUMN_USER_HEIGHT = "user_height";
    private static final String COLUMN_USER_WEIGHT = "user_weight";
    private static final String COLUMN_USER_GENDER = "user_gender";

    // Bảng data_analysis
    private static final String TABLE_DATA_ANALYSIS = "data_analysis";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_BMI = "bmi";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    // Bảng PhysicalActivity
    private static final String TABLE_PHYSICAL_ACTIVITY = "PhysicalActivity";
    private static final String COLUMN_ACTIVITY_ID = "activity_id";
    private static final String COLUMN_ACTIVITY_NAME = "activity_name";
    private static final String COLUMN_ACTIVITY_DURATION = "duration";
    private static final String COLUMN_ACTIVITY_CALORIES_BURNED = "calories_burned";

    // Bảng Total_Substances
    private static final String TABLE_TOTAL_SUBSTANCES = "total_substances";
    private static final String COLUMN_TOTAL_CALORIES = "total_calories";
    private static final String COLUMN_TOTAL_CARBS = "total_carbs";
    private static final String COLUMN_TOTAL_FAT = "total_fat";
    private static final String COLUMN_TOTAL_PROTEIN = "total_protein";
    private static final String COLUMN_CALORIES_CONSUMED = "calories_consumed";
    private static final String COLUMN_DATE = "date";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng my_user
        String createTableUser = "CREATE TABLE " + TABLE_USER + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_NAME + " TEXT, "
                + COLUMN_USER_AGE + " INTEGER, "
                + COLUMN_USER_HEIGHT + " REAL, "
                + COLUMN_USER_WEIGHT + " REAL, "
                + COLUMN_USER_GENDER + " TEXT);";
        db.execSQL(createTableUser);

        // Tạo bảng my_food
        String createTableFood = "CREATE TABLE " + TABLE_FOOD + " ("
                + COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FOOD_TITLE + " TEXT, "
                + COLUMN_FOOD_KCAL + " INTEGER, "
                + COLUMN_FOOD_CARBS + " REAL, "
                + COLUMN_FOOD_CHATBEO + " REAL, "
                + COLUMN_FOOD_CHATDAM + " REAL, "
                + COLUMN_FOOD_KHOILUONG + " REAL, "
                + COLUMN_USER_ID_FK + " INTEGER, "
                + "FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "));";
        db.execSQL(createTableFood);

        // Tạo bảng data_analysis
        String createTableDataAnalysis = "CREATE TABLE " + TABLE_DATA_ANALYSIS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID_FK + " INTEGER, "
                + COLUMN_BMI + " REAL, "
                + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "));";
        db.execSQL(createTableDataAnalysis);

        // Tạo bảng PhysicalActivity với cột calories_burned là INTEGER
        String createTablePhysicalActivity = "CREATE TABLE " + TABLE_PHYSICAL_ACTIVITY + " ("
                + COLUMN_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID_FK + " INTEGER, "
                + COLUMN_ACTIVITY_NAME + " TEXT NOT NULL, "
                + COLUMN_ACTIVITY_DURATION + " INTEGER NOT NULL, "
                + COLUMN_ACTIVITY_CALORIES_BURNED + " INTEGER NOT NULL, "
                + "FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "));";
        db.execSQL(createTablePhysicalActivity);

        // Tạo bảng total_substances để lưu trữ tổng các chất và calo tiêu thụ
        String createTableTotalSubstances = "CREATE TABLE " + TABLE_TOTAL_SUBSTANCES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID_FK + " INTEGER, "
                + COLUMN_TOTAL_CALORIES + " INTEGER, "
                + COLUMN_TOTAL_CARBS + " INTEGER, "
                + COLUMN_TOTAL_FAT + " INTEGER, "
                + COLUMN_TOTAL_PROTEIN + " INTEGER, "
                + COLUMN_CALORIES_CONSUMED + " INTEGER, "
                + COLUMN_DATE + " TEXT, "
                + "FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "));";
        db.execSQL(createTableTotalSubstances);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 11) {
            // Xóa dữ liệu trong bảng data_analysis
            db.execSQL("DELETE FROM " + TABLE_DATA_ANALYSIS);
        }
    }


    // Phương thức để kiểm tra xem đã có bản ghi nào cho ngày hiện tại chưa
    public boolean isRecordExists(int userId, String currentDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_TOTAL_SUBSTANCES + " WHERE " + COLUMN_USER_ID_FK + " = ? AND " + COLUMN_DATE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), currentDate});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public void addOrUpdateTotalSubstances(int userId, int additionalCalories, int additionalCarbs, int additionalFat, int additionalProtein, int additionalCaloriesConsumed) {
        SQLiteDatabase db = this.getWritableDatabase();
        String currentDate = getCurrentDate(); // Lấy ngày hiện tại

        if (!isRecordExists(userId, currentDate)) {
            // Nếu chưa có bản ghi cho ngày hiện tại, thêm bản ghi mới với các chỉ số ban đầu là 0
            String insertQuery = "INSERT INTO " + TABLE_TOTAL_SUBSTANCES + " (" +
                    COLUMN_USER_ID_FK + ", " +
                    COLUMN_TOTAL_CALORIES + ", " +
                    COLUMN_TOTAL_CARBS + ", " +
                    COLUMN_TOTAL_FAT + ", " +
                    COLUMN_TOTAL_PROTEIN + ", " +
                    COLUMN_CALORIES_CONSUMED + ", " +
                    COLUMN_DATE + ") VALUES (?, 0, 0, 0, 0, 0, ?)";
            db.execSQL(insertQuery, new Object[]{userId, currentDate});
        }

        // Cập nhật các chỉ số cho ngày hiện tại
        String updateQuery = "UPDATE " + TABLE_TOTAL_SUBSTANCES + " SET " +
                COLUMN_TOTAL_CALORIES + " = " + COLUMN_TOTAL_CALORIES + " + ?, " +
                COLUMN_TOTAL_CARBS + " = " + COLUMN_TOTAL_CARBS + " + ?, " +
                COLUMN_TOTAL_FAT + " = " + COLUMN_TOTAL_FAT + " + ?, " +
                COLUMN_TOTAL_PROTEIN + " = " + COLUMN_TOTAL_PROTEIN + " + ?, " +
                COLUMN_CALORIES_CONSUMED + " = " + COLUMN_CALORIES_CONSUMED + " + ? " +
                "WHERE " + COLUMN_USER_ID_FK + " = ? AND " + COLUMN_DATE + " = ?";
        db.execSQL(updateQuery, new Object[]{additionalCalories, additionalCarbs, additionalFat, additionalProtein, additionalCaloriesConsumed, userId, currentDate});
    }

    public Cursor getTotalSubstancesForDate(int userId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TOTAL_SUBSTANCES + " WHERE " + COLUMN_USER_ID_FK + " = ? AND " + COLUMN_DATE + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId), date});
    }



    public Cursor getTotalSubstancesForCurrentDate(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String currentDate = getCurrentDate(); // Lấy ngày hiện tại
        String query = "SELECT * FROM " + TABLE_TOTAL_SUBSTANCES + " WHERE " + COLUMN_USER_ID_FK + " = ? AND " + COLUMN_DATE + " = ?";
        Log.d("DatabaseCheck", "Executing SQL: " + query + " with params: " + userId + ", " + currentDate);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), currentDate});
        if (cursor.moveToFirst()) {
            int totalCaloriesIndex = cursor.getColumnIndex("total_calories");
            int totalCarbsIndex = cursor.getColumnIndex("total_carbs");
            int totalFatIndex = cursor.getColumnIndex("total_fat");
            int totalProteinIndex = cursor.getColumnIndex("total_protein");
            int caloriesConsumedIndex = cursor.getColumnIndex("calories_consumed");
            int dateIndex = cursor.getColumnIndex("date");

            if (totalCaloriesIndex != -1 && totalCarbsIndex != -1 && totalFatIndex != -1 && totalProteinIndex != -1 && caloriesConsumedIndex != -1 && dateIndex != -1) {
                int totalCalories = cursor.getInt(totalCaloriesIndex);
                int totalCarbs = cursor.getInt(totalCarbsIndex);
                int totalFat = cursor.getInt(totalFatIndex);
                int totalProtein = cursor.getInt(totalProteinIndex);
                int caloriesConsumed = cursor.getInt(caloriesConsumedIndex);
                String date = cursor.getString(dateIndex);

                Log.d("DatabaseCheck", "Fetched data: TotalCalories = " + totalCalories + ", TotalCarbs = " + totalCarbs + ", TotalFat = " + totalFat + ", TotalProtein = " + totalProtein + ", CaloriesConsumed = " + caloriesConsumed + ", Date = " + date);
            } else {
                Log.e("DatabaseCheck", "Column not found or invalid index");
            }
        } else {
            Log.d("DatabaseCheck", "No data found for userId: " + userId + " on date: " + currentDate);
        }

        return cursor;
    }


    // Phương thức để tự động lấy ngày hiện tại
    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
    public void addOrUpdateActivity(int userId, int totalCaloriesBurned) {
        SQLiteDatabase db = this.getWritableDatabase();
        String currentDate = getCurrentDate(); // Lấy ngày hiện tại

        if (isRecordExists(userId, currentDate)) {
            // Nếu đã có bản ghi cho ngày hiện tại, cập nhật calo tiêu thụ
            String query = "UPDATE " + TABLE_TOTAL_SUBSTANCES + " SET " +
                    COLUMN_CALORIES_CONSUMED + " = " + COLUMN_CALORIES_CONSUMED + " + ? " +
                    "WHERE " + COLUMN_USER_ID_FK + " = ? AND " + COLUMN_DATE + " = ?";
            db.execSQL(query, new Object[]{totalCaloriesBurned, userId, currentDate});
            Log.d("DatabaseCheck", "Updated activity for userId: " + userId + ", date: " + currentDate + ", calories burned: " + totalCaloriesBurned);
        } else {
            // Nếu chưa có bản ghi cho ngày hiện tại, thêm bản ghi mới với calo tiêu thụ
            String query = "INSERT INTO " + TABLE_TOTAL_SUBSTANCES + " (" +
                    COLUMN_USER_ID_FK + ", " +
                    COLUMN_TOTAL_CALORIES + ", " +
                    COLUMN_TOTAL_CARBS + ", " +
                    COLUMN_TOTAL_FAT + ", " +
                    COLUMN_TOTAL_PROTEIN + ", " +
                    COLUMN_CALORIES_CONSUMED + ", " +
                    COLUMN_DATE + ") VALUES (?, 0, 0, 0, 0, ?, ?)";
            db.execSQL(query, new Object[]{userId, totalCaloriesBurned, currentDate});
            Log.d("DatabaseCheck", "Inserted new activity for userId: " + userId + ", date: " + currentDate + ", calories burned: " + totalCaloriesBurned);
        }
    }





    // Phương thức để xóa thực phẩm và cập nhật chỉ số
    public void deleteFoodAndUpdateTotal(int userId, int caloriesToRemove, int carbsToRemove, int fatToRemove, int proteinToRemove, int caloriesConsumedToRemove) {
        SQLiteDatabase db = this.getWritableDatabase();
        String currentDate = getCurrentDate(); // Lấy ngày hiện tại

        if (isRecordExists(userId, currentDate)) {
            // Nếu đã có bản ghi cho ngày hiện tại, cập nhật các chỉ số
            String query = "UPDATE " + TABLE_TOTAL_SUBSTANCES + " SET " +
                    COLUMN_TOTAL_CALORIES + " = " + COLUMN_TOTAL_CALORIES + " - ?, " +
                    COLUMN_TOTAL_CARBS + " = " + COLUMN_TOTAL_CARBS + " - ?, " +
                    COLUMN_TOTAL_FAT + " = " + COLUMN_TOTAL_FAT + " - ?, " +
                    COLUMN_TOTAL_PROTEIN + " = " + COLUMN_TOTAL_PROTEIN + " - ?, " +
                    COLUMN_CALORIES_CONSUMED + " = " + COLUMN_CALORIES_CONSUMED + " - ? " +
                    "WHERE " + COLUMN_USER_ID_FK + " = ? AND " + COLUMN_DATE + " = ?";
            db.execSQL(query, new Object[]{caloriesToRemove, carbsToRemove, fatToRemove, proteinToRemove, caloriesConsumedToRemove, userId, currentDate});
        }
    }








    // Phương thức để thêm dữ liệu vào bảng my_food
    public void addFood(String title, String kcal, String carbs, String chat_beo, String chat_dam, String gram) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FOOD_TITLE, title);
        cv.put(COLUMN_FOOD_KCAL, kcal);
        cv.put(COLUMN_FOOD_CARBS, carbs);
        cv.put(COLUMN_FOOD_CHATBEO, chat_beo);
        cv.put(COLUMN_FOOD_CHATDAM, chat_dam);
        cv.put(COLUMN_FOOD_KHOILUONG, gram);
        cv.put(COLUMN_USER_ID_FK, 1); // Thiết lập mặc định là 1

        long result = db.insert(TABLE_FOOD, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to Add Data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Data Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức để đọc dữ liệu từ bảng my_food
    public Cursor readAllFoodData() {
        String query = "SELECT * FROM " + TABLE_FOOD;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    // Phương thức để thêm dữ liệu vào bảng my_user
    public void addUser(String name, String age, String height, String weight, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra xem có người dùng nào đã tồn tại hay chưa
        String query = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            // Đã có người dùng trong bảng, không cho phép thêm người dùng mới
            Toast.makeText(context, "Đã có người dùng không thể thêm ", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        cursor.close();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_NAME, name);
        cv.put(COLUMN_USER_AGE, age);
        cv.put(COLUMN_USER_HEIGHT, height);
        cv.put(COLUMN_USER_WEIGHT, weight);
        cv.put(COLUMN_USER_GENDER, gender);

        long result = db.insert(TABLE_USER, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to Add User", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "User Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức để cập nhật dữ liệu bảng thực phẩm
    public void updateFood(String id, String title, String kcal, String carbs, String fat, String protein, String gram, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FOOD_TITLE, title);
        cv.put(COLUMN_FOOD_KCAL, kcal);
        cv.put(COLUMN_FOOD_CARBS, carbs);
        cv.put(COLUMN_FOOD_CHATBEO, fat);
        cv.put(COLUMN_FOOD_CHATDAM, protein);
        cv.put(COLUMN_FOOD_KHOILUONG, gram);
        cv.put(COLUMN_USER_ID_FK, userId);
        db.update(TABLE_FOOD, cv, COLUMN_FOOD_ID + "=?", new String[]{id});
    }

    // Phương thức để xóa một hàng trong bảng my_food
    void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_FOOD, COLUMN_FOOD_ID + "=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức để cập nhật dữ liệu người dùng
    public void updateUser(String id, String name, String age, String height, String weight, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_NAME, name);
        cv.put(COLUMN_USER_AGE, age);
        cv.put(COLUMN_USER_HEIGHT, height);
        cv.put(COLUMN_USER_WEIGHT, weight);
        cv.put(COLUMN_USER_GENDER, gender);
        db.update(TABLE_USER, cv, COLUMN_USER_ID + "=?", new String[]{id});
    }

    // Phương thức để xóa người dùng
    public void deleteUser(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_USER, COLUMN_USER_ID + "=?", new String[]{userId});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức để đọc dữ liệu từ bảng my_user
    public Cursor readAllUserData() {
        String query = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public String getFormattedDate(String timestamp) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(timestamp);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    // Phương thức chuyển đổi chiều cao từ inch sang mét
    public double convertHeightToMeters(double heightInCm) {
        return heightInCm / 100;
    }

    public double calculateBMI(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_height, user_weight FROM " + TABLE_USER + " WHERE user_id = ?", new String[]{String.valueOf(userId)});
        double bmi = 0;
        if (cursor.moveToFirst()) {
            int heightIndex = cursor.getColumnIndex("user_height");
            int weightIndex = cursor.getColumnIndex("user_weight");

            if (heightIndex != -1 && weightIndex != -1) {
                double heightInCm = cursor.getDouble(heightIndex);
                double weightInKg = cursor.getDouble(weightIndex);

                // Chuyển đổi chiều cao từ cm sang mét
                double height = convertHeightToMeters(heightInCm);

                if (height > 0) { // Đảm bảo chiều cao không bằng 0 để tránh chia cho 0
                    bmi = weightInKg / (height * height);
                } else {
                    Log.e("Database Error", "Height is zero or less");
                }
            } else {
                Log.e("Database Error", "Column not found");
            }
        }
        cursor.close();
        return bmi;
    }

    // Phương thức để thêm dữ liệu vào bảng data_analysis
    public void AddBMI(double bmi) {
        int userId = 1; // Thiết lập mặc định là 1
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_ID_FK, userId);
        cv.put(COLUMN_BMI, bmi);
        long result = db.insert(TABLE_DATA_ANALYSIS, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to Add BMI", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "BMI Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public String getBMISuggestion(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_height, user_weight FROM " + TABLE_USER + " WHERE user_id = ?", new String[]{String.valueOf(userId)});
        String suggestion = "";
        if (cursor.moveToFirst()) {
            int heightIndex = cursor.getColumnIndex("user_height");
            int weightIndex = cursor.getColumnIndex("user_weight");

            if (heightIndex != -1 && weightIndex != -1) {
                double height = cursor.getDouble(heightIndex);
                double weight = cursor.getDouble(weightIndex);
                double bmi = weight / (height * height);

                if (bmi < 18.5) {
                    suggestion = "Bạn đang bị thiếu cân. hãy ăn uống đầy đủ chất và tập luyện đầy đủ.";
                } else if (bmi >= 18.5 && bmi < 24.9) {
                    suggestion = "Cân nặng của bạn bình thường. Hãy duy trì ăn uống và tập luyện thường xuyên.";
                } else if (bmi >= 25 && bmi < 29.9) {
                    suggestion = "Bạn đang bị thừa cân. Hãy duy trì tập luyện nhiều hơn và chế độ ăn uống lành mạnh.";
                } else if (bmi >= 30) {
                    suggestion = "Bạn đang béo phì. Hãy tìm bác sĩ chuyên môn để tham khảo ý kiến.";
                }
            } else {
                Log.e("Database Error", "Column not found");
            }
        }
        cursor.close();
        return suggestion;
    }
    // Phương thức để lấy dữ liệu BMI mới nhất từ bảng data_analysis
    public Cursor getLatestBMIData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT *, DATE(" + COLUMN_TIMESTAMP + ") as formatted_date FROM " + TABLE_DATA_ANALYSIS + " WHERE " + COLUMN_USER_ID_FK + " = 1 ORDER BY " + COLUMN_TIMESTAMP + " DESC LIMIT 1";
        return db.rawQuery(query, null);
    }

//phương thức xóa BMI
    public void deleteBMI(String bmiId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_DATA_ANALYSIS, COLUMN_ID + "=?", new String[]{bmiId});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete BMI.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted BMI.", Toast.LENGTH_SHORT).show();
        }
    }
    public void addPhysicalActivity(String activityName, String duration, String caloriesBurned) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        try {
            int durationInt = Integer.parseInt(duration);
            int caloriesBurnedInt = Integer.parseInt(caloriesBurned);

            // Log dữ liệu đã chuyển đổi
            Log.d("DatabaseCheck", "Activity Name: " + activityName);
            Log.d("DatabaseCheck", "Duration (int): " + durationInt);
            Log.d("DatabaseCheck", "Calories Burned (int): " + caloriesBurnedInt);

            cv.put(COLUMN_ACTIVITY_NAME, activityName);
            cv.put(COLUMN_ACTIVITY_DURATION, durationInt);
            cv.put(COLUMN_ACTIVITY_CALORIES_BURNED, caloriesBurnedInt);
            cv.put(COLUMN_USER_ID_FK, 1); // Thiết lập mặc định là 1

            long result = db.insert(TABLE_PHYSICAL_ACTIVITY, null, cv);
            if (result == -1) {
                Toast.makeText(context, "Failed to Add Activity", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Activity Added Successfully", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(context, "Please enter valid integer values for duration and calories burned", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }



    public void updatePhysicalActivity(int activityId, String activityName, int duration, double caloriesBurned) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_NAME, activityName);
        values.put(COLUMN_ACTIVITY_DURATION, duration);
        values.put(COLUMN_ACTIVITY_CALORIES_BURNED, caloriesBurned);

        int result = db.update(TABLE_PHYSICAL_ACTIVITY, values, COLUMN_ACTIVITY_ID + "=?", new String[]{String.valueOf(activityId)});
        if (result == 0) {
            Log.e("UpdateError", "Failed to update activity with ID: " + activityId);
        }
        db.close();
    }

    // Phương thức xóa hoạt động thể chất sử dụng biến tĩnh trực tiếp
        public void deletePhysicalActivity(int id) {
            SQLiteDatabase db = this.getWritableDatabase();
            long result = db.delete(TABLE_PHYSICAL_ACTIVITY, COLUMN_ACTIVITY_ID + "=?", new String[]{String.valueOf(id)});
            if (result == -1) {
                Toast.makeText(context, "Failed to Delete Activity.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Successfully Deleted Activity.", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }


    //phương thức hiển thị BMI
    public Cursor readAllDataAnalysis() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_BMI + ", " + COLUMN_TIMESTAMP + ", " +
                "CASE " +
                "WHEN " + COLUMN_BMI + " < 18.5 THEN 'Bạn đang bị thiếu cân. hãy ăn uống đầy đủ chất và tập luyện đầy đủ.' " +
                "WHEN " + COLUMN_BMI + " BETWEEN 18.5 AND 24.9 THEN 'Cân nặng của bạn bình thường. Hãy duy trì ăn uống và tập luyện thường xuyên.' " +
                "WHEN " + COLUMN_BMI + " BETWEEN 25 AND 29.9 THEN 'Bạn đang bị thừa cân. Hãy duy trì tập luyện nhiều hơn và chế độ ăn uống lành mạnh.' " +
                "WHEN " + COLUMN_BMI + " >= 30 THEN 'Bạn đang béo phì. Hãy tìm bác sĩ chuyên môn để tham khảo ý kiến.' " +
                "ELSE 'No suggestion available' END AS suggestion " +
                "FROM " + TABLE_DATA_ANALYSIS;
        return db.rawQuery(query, null);
    }
    public Cursor readAllPhysicalActivitiesByUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PHYSICAL_ACTIVITY + " WHERE " + COLUMN_USER_ID_FK + " = " + userId + " ORDER BY " + COLUMN_ACTIVITY_ID + " DESC";
        Log.d("DatabaseQuery", "Executing query: " + query); // Log câu truy vấn
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            if (cursor.getCount() == 0) {
                Log.d("DatabaseQuery", "No data found for user ID: " + userId);
            } else {
                Log.d("DatabaseQuery", "Data found for user ID: " + userId);
            }
        }
        return cursor;
    }


}