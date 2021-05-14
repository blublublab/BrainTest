package com.demo.braintest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class GameOverActivity extends AppCompatActivity {
    private TextView textViewFinalResult;
    private TextView textViewMaxResult;
    private int maxScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        textViewFinalResult = findViewById(R.id.textViewFinalResult);
        textViewMaxResult = findViewById(R.id.textViewMaxResult);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        maxScore  = preferences.getInt("maxScore", 0);

        //PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent =  getIntent();
        int score = intent.getIntExtra("score", 0);
        textViewFinalResult.setText(String.format(Locale.getDefault() , "Your final score is:  %d", score));
        textViewMaxResult.setText(String.format(Locale.getDefault(), "Your max score is: %d", maxScore));
    }

    public void onClickRestart(View view) {
        Intent startIntent = new Intent(GameOverActivity.this, MainActivity.class);
        startActivity(startIntent);
    }
}