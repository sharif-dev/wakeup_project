package com.example.wakeup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class birdActivity extends AppCompatActivity{


    private Integer birdCount = 0;
    private  Integer threshold;
    private TextView textView;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird);
        editor = getSharedPreferences("alarmInfo", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("alarmInfo", MODE_PRIVATE);
        threshold = 5 + prefs.getInt("difficulty", 3);



        textView = (TextView) findViewById(R.id.remainigTaps);
        Integer temp = threshold - birdCount;
        textView.setText(temp.toString());


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

        final ImageView[] birds = {
                (ImageView) findViewById(R.id.bird1),
                (ImageView) findViewById(R.id.bird2),
                (ImageView) findViewById(R.id.bird3),
                (ImageView) findViewById(R.id.bird4),
                (ImageView) findViewById(R.id.bird5),
                (ImageView) findViewById(R.id.bird6),
                (ImageView) findViewById(R.id.bird7),
                (ImageView) findViewById(R.id.bird8),
                (ImageView) findViewById(R.id.bird9),
                (ImageView) findViewById(R.id.bird10),
                (ImageView) findViewById(R.id.bird11),
                (ImageView) findViewById(R.id.bird12),
                (ImageView) findViewById(R.id.bird13),
                (ImageView) findViewById(R.id.bird14),
                (ImageView) findViewById(R.id.bird15),
                (ImageView) findViewById(R.id.bird16),
        };

        for (int count = 0; count < 16 ; count++){
            final int counter = count;
            birds[count].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    birdCount ++;
                    birds[counter].setVisibility(View.INVISIBLE);
                    Integer temp = threshold - birdCount;
                    textView.setText(temp.toString());
                    if (threshold - birdCount == 0){
                        vibrator.cancel();
                        r.stop();
                        editor.putBoolean("isAnyAlarmSet", false);
                        editor.putString("alarmType","");
                        editor.apply();
                        Intent startIntent = new Intent(getApplicationContext(), SettingActivity.class);
                        startIntent.putExtra("type","bird");
                        startActivity(startIntent);
                    }

                }
            });
        }

    }
}
