package com.example.wakeup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.FileAlreadyExistsException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class mathGameActivity extends AppCompatActivity {
    TextView textTimer;
    int time;
    TextView equation_text ;
    Button false_button ;
    Button true_button ;
    Boolean ansIsCorrect = false;
    Integer gameNum = 0;
    Integer correctAnsNum = 0;
    Integer incorrectAnsNum = 0;
    TextView score_num_txt ;
    int score = 0;
    int threshold = 5;
    ImageView lose_image ;
    double a = 0.4;
    double mean_score = 0;
    int final_time ;
    CountDownTimer timer;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_game);
        editor = getSharedPreferences("alarmInfo", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("alarmInfo", MODE_PRIVATE);
        threshold = prefs.getInt("difficulty", 3);
        //setting vibration
        final Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
        vibrator.vibrate(pattern, 0);

        //playing ringtone
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(true);
        }

        time=7;
        final_time = 7;
        lose_image = findViewById(R.id.lose_image);
        lose_image.setVisibility(View.INVISIBLE);
        score_num_txt = findViewById(R.id.score_num);
        score_num_txt.setText(""+score);
        textTimer = (TextView)findViewById(R.id.timer);
        equation_text = findViewById(R.id.equation_text);
        false_button = findViewById(R.id.button_false);
        true_button = findViewById(R.id.true_button);
        equation_text.setText(get_random_equation());

        false_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ansIsCorrect){
                    incorrectAnsNum +=1;
                    score-=1;
                    score_num_txt.setText(""+score);
                    equation_text.setText(get_random_equation());
                }else{
                    score+=1;
                    score_num_txt.setText(""+score);
                    correctAnsNum +=1;
                    equation_text.setText(get_random_equation());
                }
                System.out.println("------------------------------------ score = "+score);
            }
        });
        true_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ansIsCorrect){
                    correctAnsNum+=1;
                    score+=1;
                    score_num_txt.setText(""+score);
                    equation_text.setText(get_random_equation());
                }else{
                    incorrectAnsNum +=1;
                    score-=1;
                    score_num_txt.setText(""+score);
                    equation_text.setText(get_random_equation());
                }
                System.out.println("------------------------------------------------------------------score= "+score);
            }
        });
        timer = new CountDownTimer(final_time*1500+1000 , 1500) {
            public void onTick(long millisUntilFinished) {
                textTimer.setText("0:"+checkDigit(time));
                time--; }
            public void onFinish() {System.out.println(threshold +" "+ score);
                if (score >= threshold){
                    vibrator.cancel();
                    r.stop();
                    editor.putBoolean("isAnyAlarmSet", false);
                    editor.putString("alarmType","");
                    editor.apply();
                    Intent startIntent = new Intent(getApplicationContext(), SettingActivity.class);
                    startIntent.putExtra("type","math");
                    startActivity(startIntent);
                }
                else{
                    mean_score =a* mean_score + (1-a) *score;
                    gameNum +=1;
                    if (gameNum >= 4) {
                        if (mean_score >=2){
                            //todo
                            vibrator.cancel();
                            r.stop();
                            editor.putBoolean("isAnyAlarmSet", false);
                            editor.putString("alarmType","");
                            editor.apply();
                            Intent startIntent = new Intent(getApplicationContext(), SettingActivity.class);
                            startIntent.putExtra("type","math");
                            startActivity(startIntent);
                        }
                        else {
                            Toast.makeText(mathGameActivity.this,"Game over , try again :) ",Toast.LENGTH_LONG).show();
                            gameNum = 0;
                        }
                    }
//
                    score = 0;
                    score_num_txt.setText(""+score);
                    Toast.makeText(mathGameActivity.this," try again !",Toast.LENGTH_LONG).show();
                    time=final_time;
                    textTimer.setText("0:"+checkDigit(time));
                    timer.start();


                }
            }
        };
        timer.start();


    }
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
    public String get_random_equation (){
        ansIsCorrect = false;
        Random rn = new Random();
        Integer ansdef = rn.nextInt(3) -1;
        if (ansdef == 0){
            ansIsCorrect = true;
        }
        Integer num1 = rn.nextInt(8 ) +7;
        Integer num2 = rn.nextInt(7 ) +2;
        Integer type = rn.nextInt(2 );
        String finalString = num1+" ";
        Integer myAns ;

        if (type == 0){
            finalString+="+ ";
            myAns = num1+num2+ansdef;
        }else{
            finalString+="- ";
            myAns = num1-num2+ansdef;
        }
        finalString+=num2+" = "+myAns;
        return finalString;
    }
}
