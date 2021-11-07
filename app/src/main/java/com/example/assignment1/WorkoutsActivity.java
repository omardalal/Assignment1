package com.example.assignment1;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WorkoutsActivity extends AppCompatActivity {

    private ListView workoutsListView;

    private String type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);
        workoutsListView = findViewById(R.id.workouts_list);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        setupActionBar();

        setupWorkoutsListView();
    }

    private void setupWorkoutsListView() {
        ArrayList<Workout> favWorkouts = new ArrayList<>();
        if (type.equals("fav")) {
            for (Workout w: MainActivity.workouts) {
                if (w.getFavorite()) {
                    favWorkouts.add(w);
                }
            }
        }
        workoutsListView.setAdapter(new ArrayAdapter<Workout>(this, android.R.layout.simple_list_item_1, type.equals("all")?MainActivity.workouts:favWorkouts));
        workoutsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(WorkoutsActivity.this, WorkoutDetails.class);
                Workout workout = (Workout) workoutsListView.getAdapter().getItem(i);
                intent.putExtra("id", workout.getId());
                intent.putExtra("srcType", type);
                startActivity(intent);
            }
        });
    }

    private boolean favView = false;
    private void setupActionBar() {
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (type.equals("all")) {
            actionbar.setTitle(R.string.all_workouts_title);
        } else {
            favView = true;
            actionbar.setTitle(R.string.fav_workouts_title);
        }
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
        if (item.getItemId() == R.id.add_top_btn) {
            Intent intent = new Intent(this, AddWorkout.class);
            intent.putExtra("srcActivity", "workout");
            intent.putExtra("srcType", favView?"fav":"all");
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}