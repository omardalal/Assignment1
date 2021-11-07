package com.example.assignment1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static boolean loadedWorkouts = false;
    public static ArrayList<Workout> workouts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.main_title);

        if (!loadedWorkouts) {
            loadWorkouts();
        }
    }

    private void loadWorkouts() {
        SharedPreferences preferences = getSharedPreferences("defaultWorkouts", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("defaultWorkouts", null);
        ArrayList<Workout> defaultWorkouts;
        if (json == null) {
            saveDefaultWorkouts();
        } else {
            defaultWorkouts = gson.fromJson(json, new TypeToken<ArrayList<Workout>>() {
            }.getType());
            for (Workout w : defaultWorkouts) {
                workouts.add(w);
            }
        }
        loadedWorkouts = true;
    }

    private void saveDefaultWorkouts() {
        String[] titles = getResources().getStringArray(R.array.workoutTitles);
        int[] durations = getResources().getIntArray(R.array.workoutDurations);
        String[] categories = getResources().getStringArray(R.array.workoutCategories);
        String[] descriptions = getResources().getStringArray(R.array.workoutDescriptions);
        for (int i = 0; i < titles.length; i++) {
            workouts.add(new Workout(titles[i], durations[i], categories[i], descriptions[i], i));
        }
        saveWorkouts();
    }

    private void saveWorkouts() {
        SharedPreferences preferences = getSharedPreferences("defaultWorkouts", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(workouts);
        editor.putString("defaultWorkouts", json);
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveWorkouts();
    }

    public void allClick(View view) {
        Intent intent = new Intent(this, WorkoutsActivity.class);
        intent.putExtra("type", "all");
        startActivity(intent);
    }

    public void favClick(View view) {
        Intent intent = new Intent(this, WorkoutsActivity.class);
        intent.putExtra("type", "fav");
        startActivity(intent);
    }

    public void createClick(View view) {
        Intent intent = new Intent(this, AddWorkout.class);
        intent.putExtra("srcActivity", "main");
        startActivity(intent);
    }
}