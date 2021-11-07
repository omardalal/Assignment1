package com.example.assignment1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;

public class WorkoutDetails extends AppCompatActivity {

    private TextView nameTextView;
    private TextView durationTextView;
    private TextView categoryTextView;
    private TextView descriptionTextView;
    private CheckBox favoriteCheckBox;
    private Workout w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details);
        nameTextView = findViewById(R.id.workoutName);
        durationTextView = findViewById(R.id.workoutDuration);
        categoryTextView = findViewById(R.id.workoutCategory);
        descriptionTextView = findViewById(R.id.workoutDetails);
        favoriteCheckBox = findViewById(R.id.favoriteCheckbox);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.workoutDetailsTitle);

        fillData();

        favoriteCheckBox.setOnClickListener(e-> {
            int i=0;
            for (i=0; i<MainActivity.workouts.size(); i++) {
                if (MainActivity.workouts.get(i).getId()==w.getId()) {
                    break;
                }
            }
            MainActivity.workouts.get(i).setFavorite(favoriteCheckBox.isChecked());
        });
    }

    private void fillData() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        w = getWorkoutById(id);
        if (w!=null) {
            nameTextView.setText(w.getName());
            durationTextView.setText(w.getDuration()+" minutes");
            categoryTextView.setText(w.getCategory());
            descriptionTextView.setText(w.getDescription());
            favoriteCheckBox.setChecked(w.getFavorite());
        }
    }

    private Workout getWorkoutById(int id) {
        for (Workout w: MainActivity.workouts) {
            if (w.getId()==id) {
                return w;
            }
        }
        return null;
    }

    private void saveWorkouts() {
        SharedPreferences preferences = getSharedPreferences("defaultWorkouts", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.workouts);
        editor.putString("defaultWorkouts", json);
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveWorkouts();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent prevIntent = getIntent();
        Class targetClass = WorkoutsActivity.class;
        targetClass = item.getItemId() == R.id.timer_top_btn ?  TimerActivity.class : targetClass;

        Intent intent = new Intent(this, targetClass);
        if (item.getItemId() == R.id.timer_top_btn) {
            intent.putExtra("srcType", prevIntent.getStringExtra("srcType"));
            intent.putExtra("id", w.getId());
            intent.putExtra("duration", w.getDuration());
            intent.putExtra("srcType", prevIntent.getStringExtra("srcType"));
        } else {
            intent.putExtra("type", prevIntent.getStringExtra("srcType"));
        }
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }
}