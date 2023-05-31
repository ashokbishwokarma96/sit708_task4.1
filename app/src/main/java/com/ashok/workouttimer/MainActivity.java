package com.ashok.workouttimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText workoutDurationEditText, restDurationEditText;
    private TextView workoutTimerTextView, restTimerTextView;
    private ProgressBar progressBar;
    private Button startButton, stopButton;
    private CountDownTimer workoutTimer, restTimer;

    private boolean isWorkoutTimerRunning = false;
    private boolean isRestTimerRunning = false;
    private int workoutDuration = 0;
    private int restDuration = 0;
    private int count=0;
    Intent stopTimerIntent;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize UI components
        workoutDurationEditText = findViewById(R.id.workoutDurationEditText);
        restDurationEditText = findViewById(R.id.restDurationEditText);
        workoutTimerTextView = findViewById(R.id.workoutTimerTextView);
        restTimerTextView = findViewById(R.id.restTimerTextView);
        progressBar = findViewById(R.id.progressBar);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
         stopTimerIntent = new Intent(this, MainActivity.class);
         context = this;


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });
    }
    private void startTimer() {
        // Parse duration values from EditTexts
        workoutDuration =  workoutDurationEditText.getText().toString().isEmpty() ? 0 : Integer.parseInt(workoutDurationEditText.getText().toString());
        restDuration = restDurationEditText.getText().toString().isEmpty() ? 0 : Integer.parseInt(restDurationEditText.getText().toString());
if(workoutDuration<=0 ){
    Toast.makeText(this, "empty value", Toast.LENGTH_SHORT).show();
    return;
}

        // Initialize workout and rest timers
        workoutTimer = new CountDownTimer(workoutDuration * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) millisUntilFinished / 1000;
                updateWorkoutTimer(secondsRemaining);
            }

            @Override
            public void onFinish() {
                isWorkoutTimerRunning = false;
                startRestTimer();
            }
        };
        restTimer = new CountDownTimer(restDuration * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) millisUntilFinished / 1000;
                updateRestTimer(secondsRemaining);
            }

            @Override
            public void onFinish() {
                isRestTimerRunning = false;
                startWorkoutTimer();
                stopTimerIntent.setAction("STOP_TIMER");
                new NotificationUtil().showNotification(context,count,"stopwatch","timmer is off now",R.drawable.baseline_alarm,stopTimerIntent,
                        true);

            }
        };
        startWorkoutTimer();
        // Disable EditTexts and start button
        workoutDurationEditText.setEnabled(false);
        restDurationEditText.setEnabled(false);
        startButton.setEnabled(false);
    }
    private void startWorkoutTimer() {
        isWorkoutTimerRunning = true;
        workoutTimer.start();
    }

    private void startRestTimer() {
        isRestTimerRunning = true;
        restTimer.start();
    }
    private void stopTimer() {
        // Cancel workout and rest timers
        if (workoutTimer != null) {
            workoutTimer.cancel();
        }

        if (restTimer != null) {
            restTimer.cancel();
        }


        // Reset progress bar and text views
        progressBar.setProgress(0);
        workoutTimerTextView.setText("00:00");
        restTimerTextView.setText("00:00");

        // Enable EditTexts and start button
        workoutDurationEditText.setEnabled(true);
        restDurationEditText.setEnabled(true);

            startButton.setEnabled(true);
            count++;

//        public static void showNotification(Context context, int notificationId, String title, String message, int iconResId, Intent intent, boolean autoCancel)

        }
    private void updateWorkoutTimer(int secondsRemaining) {
        // Update progress bar
        int progress = (workoutDuration - secondsRemaining) * 100 / workoutDuration;
        progressBar.setProgress(progress);

        // Calculate minutes and seconds remaining
        int minutes = secondsRemaining / 60;
        int seconds = secondsRemaining % 60;
        // Update workout timer text view
        String workoutTimerText = getString(R.string.workout_timer_format,  minutes, seconds);
        workoutTimerTextView.setText(workoutTimerText);
    }

    private void updateRestTimer(int secondsRemaining) {
        // Update progress bar
        int progress = (restDuration - secondsRemaining) * 100 / restDuration;
        progressBar.setProgress(progress);

        // Calculate minutes and seconds remaining
        int minutes = secondsRemaining / 60;
        int seconds = secondsRemaining % 60;

        // Update rest timer text view
        String restTimerText = getString(R.string.rest_timer_format, minutes, seconds);
        restTimerTextView.setText(restTimerText);
    }
}