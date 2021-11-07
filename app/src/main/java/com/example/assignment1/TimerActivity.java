package com.example.assignment1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {

    private int id;
    private int duration;
    private boolean timerRunning = false;
    private boolean done = false;
    private TextView timerLbl;
    private Button startPauseBtn;
    private Thread thread;
    private WorkoutTimerTask timerTask;
    private int timerColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        initializeVars();
        initializeActionBar();
        timerLbl.setText(formatTime(duration));
        timerColor = timerLbl.getCurrentTextColor();
    }

    private void initializeActionBar() {
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.timerPageTitle);
    }

    private void initializeVars() {
        timerLbl = findViewById(R.id.timerLbl);
        startPauseBtn = findViewById(R.id.startBtn);
        Intent prevIntent = getIntent();
        id = prevIntent.getIntExtra("id", 0);
        duration = prevIntent.getIntExtra("duration", 0)*60;
        timerTask = new WorkoutTimerTask(duration);
        thread = new Thread(timerTask);
        thread.start();
    }

    public void onStartPauseClick(View view) {
        if (timerRunning) {
            startPauseBtn.setText(R.string.timerStartBtn);
        } else {
            startPauseBtn.setText(R.string.timerPauseBtn);
        }
        timerRunning = !timerRunning;
    }

    public void onResetClick(View view) {
        timerLbl.setTextColor(timerColor);
        timerTask.resetTime();
        startPauseBtn.setText(R.string.timerStartBtn);
        timerRunning = false;
        startPauseBtn.setEnabled(true);
        if (done) {
            done = false;
            thread = new Thread(timerTask);
            thread.start();
        }
        timerLbl.setText(formatTime(duration));
    }

    public String formatTime(int time) {
        String minutes = String.format("%2d", time/60).replace(" ", "0");;
        String seconds = String.format("%2d", time%60).replace(" ", "0");;
        return minutes+":"+seconds;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent prevIntent = getIntent();
        Intent intent = new Intent(this, WorkoutDetails.class);
        intent.putExtra("id", id);
        intent.putExtra("srcType", prevIntent.getStringExtra("srcType"));
        startActivity(intent);
        return true;
    }

    class WorkoutTimerTask implements Runnable {
        private int time;
        private int timeLeft;
        public WorkoutTimerTask (int time) {
            this.time = time;
            this.timeLeft = time;
        }
        @Override
        public void run() {
            while (timeLeft>0) {
                if (timerRunning) {
                    try {
                        Thread.sleep(1000);
                        timerLbl.post(new Runnable() {
                            @Override
                            public void run() {
                                timerLbl.setText(formatTime(timeLeft));
                            }
                        });
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    timeLeft--;
                }
            }
            done = true;
            startPauseBtn.post(new Runnable() {
                @Override
                public void run() {
                    startPauseBtn.setEnabled(false);
                }
            });
            timerLbl.post(new Runnable() {
                @Override
                public void run() {
                    timerLbl.setTextColor(ContextCompat.getColor(TimerActivity.this, R.color.timerRed));
                }
            });
        }
        public void resetTime() {
            timeLeft = time;
            timerLbl.setText(formatTime(0));
        }
    }
}