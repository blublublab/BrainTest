package com.demo.braintest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static com.demo.braintest.R.string.correct_toast_text;
import static com.demo.braintest.R.string.wrong_toast_text;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTimer;
    private TextView textViewExample;
    private TextView textViewAnswerCount;

    private StringBuilder operator = new StringBuilder();
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button[] buttons;

    private int maxScore;
    private int correctAnswers = 0;
    private int allAnswers = 0;
    private int answer;
    private int correctButton;
    private final int MIN_NUMBER = 5;
    private final int MAX_NUMBER = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        maxScore = getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE).getInt("maxScore", 0);
        setContentView(R.layout.activity_main);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewExample = findViewById(R.id.textViewExample);
        textViewAnswerCount = findViewById(R.id.textViewAnswerCount);
        button1 = findViewById(R.id.buttonAnswer1);
        button2 = findViewById(R.id.buttonAnswer2);
        button3 = findViewById(R.id.buttonAnswer3);
        button4 = findViewById(R.id.buttonAnswer4);
        buttons = new Button[]{button1, button2, button3, button4};
        startTimer();
        startGame();

    }

    public void startGame(){
        operator.setLength(0);
        textViewAnswerCount.setText(String.format(Locale.getDefault(), "%d / %d", correctAnswers, allAnswers));
        int firstDigit = (int) (Math.random()*(MAX_NUMBER-MIN_NUMBER   + 1 )  + MIN_NUMBER);
        int secondDigit;
        do{
            secondDigit = (int) (Math.random()*(MAX_NUMBER-MIN_NUMBER  + 1  )+  MIN_NUMBER);
        }while(secondDigit == firstDigit);
        int randomOperator = randomOperator();
        answer = firstDigit + ( randomOperator*secondDigit );
        if(randomOperator == 1) {
            operator.append("+");
        } else if( randomOperator == -1 ) {

            operator.append("-");
        }
        textViewExample.setText( String.format( Locale.getDefault() , "%d %s %d " , firstDigit, operator.toString() , Math.abs(secondDigit)) );
        //Кнопки заполняем числами
        int generateInt;
        for(int i = 0; i < buttons.length; i++){
            do {
                generateInt = (int) (Math.random() * (MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER);
            } while(generateInt == answer);

            buttons[i].setText(String.valueOf(generateInt));
        }
        //НЕ УВЕРЕН 3 ВКЛЮЧИТЕЛЬНО ИЛИ НЕТ
        correctButton = (int) (Math.random()*buttons.length);
        buttons[correctButton].setText(String.valueOf(answer));

    }


    public int randomOperator(){
      int random = (int) Math.round( Math.random());

      if(random  == 0) {
          Log.i("LOG", String.valueOf(random));
          return -1;
      } else if(random == 1){
          Log.i("LOG", String.valueOf(random));
          return 1;
      }
      Log.i("LOG", "Error of input randomOperator");
      return 0;
    }
    public void startTimer(){
        CountDownTimer count  = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(String.valueOf( (int) (millisUntilFinished/1000)));
            }

            @Override
            public void onFinish() {
                //Final score intent save
                Intent intent  = new Intent(MainActivity.this, GameOverActivity.class);
                intent.putExtra("score", correctAnswers );
                startActivity(intent);
                //Final score global save;

                //Под вопросом maxScore == 0 ??
                if(maxScore < correctAnswers || maxScore == 0) {
                    maxScore = correctAnswers;
                    SharedPreferences preferences =  getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
                    preferences.edit().putInt("maxScore", maxScore).apply();

                }

            }
        };
        count.start();
    }

    public void onClickAnswer(View view) {
        int id =  R.id.buttonAnswer1;
        Toast toastRight =  Toast.makeText(this, correct_toast_text, Toast.LENGTH_SHORT);
        Toast toastWrong = Toast.makeText(this, wrong_toast_text, Toast.LENGTH_SHORT);
        if (view.getId() == buttons[correctButton].getId() ) {
            toastWrong.cancel();
            toastRight.show();
            correctAnswers++;
        } else {
            toastRight.cancel();
            toastWrong.show();
    }
        allAnswers++;
        startGame();
    }
}