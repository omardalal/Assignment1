package com.example.assignment1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddWorkout extends AppCompatActivity {

    private EditText nameField;
    private EditText durationField;
    private Spinner categorySpinner;
    private EditText descriptionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        nameField = findViewById(R.id.nameField);
        durationField = findViewById(R.id.durationField);
        categorySpinner = findViewById(R.id.categorySpinner);
        descriptionField = findViewById(R.id.descriptionField);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.create_title);
    }

    public void onAddClicked(View view) {
        if (nameField.getText().toString().trim().isEmpty() ||
            durationField.getText().toString().trim().isEmpty() ||
            descriptionField.getText().toString().trim().isEmpty()
        ) {
            Toast.makeText(this, "Please fill all fields!",Toast.LENGTH_LONG).show();
        } else {
            String name = nameField.getText().toString();
            int duration = Integer.parseInt(durationField.getText().toString());
            String category = categorySpinner.getSelectedItem().toString();
            String description = descriptionField.getText().toString();
            Workout workout = new Workout(name, duration, category, description, MainActivity.workouts.size()+1);
            Intent prevIntent = getIntent();
            if (prevIntent.getStringExtra("srcType")!=null&&prevIntent.getStringExtra("srcType").equals("fav")) {
                workout.setFavorite(true);
            }
            MainActivity.workouts.add(workout);
            backPressed();
        }
    }

    public void backPressed() {
        Intent prevIntent = getIntent();
        Class targetActivity;
        String viewType = "all";
        if (prevIntent.getStringExtra("srcActivity").equals("main")) {
            targetActivity = MainActivity.class;
        } else {
            targetActivity = WorkoutsActivity.class;
            if (prevIntent.getStringExtra("srcType").equals("fav")) {
                viewType = "fav";
            }
        }
        Intent intent = new Intent(getApplicationContext(), targetActivity);
        if (targetActivity.equals(WorkoutsActivity.class)) {
            intent.putExtra("type", viewType);
        }
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        backPressed();
        return true;
    }
}