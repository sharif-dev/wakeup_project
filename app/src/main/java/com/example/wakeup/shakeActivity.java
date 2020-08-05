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
import android.widget.Button;
import android.widget.TextView;

public class shakeActivity extends AppCompatActivity  {
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private Button back;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    int threshold ;
    int temp = 3;
    TextView num_shake;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
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

        back = (Button) findViewById(R.id.backBTN);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.cancel();
                r.stop();
                editor.putBoolean("isAnyAlarmSet", false);
                editor.putString("alarmType","");
                editor.apply();
                Intent startIntent = new Intent(getApplicationContext(), SettingActivity.class);
                startIntent.putExtra("type","shake");
                startActivity(startIntent);
            }
        });

        editor = getSharedPreferences("alarmInfo", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("alarmInfo", MODE_PRIVATE);
        threshold = 1 + prefs.getInt("difficulty", 3);
        num_shake = findViewById(R.id.num_shake_txt);
        num_shake.setText(""+threshold);

        //setting gyroscope sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if(event.values[2] > 0.5f*temp || event.values[2] < -0.5f*temp){
//                    System.out.println("hi--------------------------> " + temp);
                    temp-=1;
                    if (temp == 0) {
                        temp = 3;
                        threshold -= 1;
                        num_shake.setText("" + threshold);
                        if (threshold == 0) {
                            vibrator.cancel();
                            r.stop();
                            editor.putBoolean("isAnyAlarmSet", false);
                            editor.putString("alarmType", "");
                            editor.apply();
                            Intent startIntent = new Intent(getApplicationContext(), SettingActivity.class);
                            startIntent.putExtra("type", "shake");
                            startActivity(startIntent);
                        }
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(sensorEventListener, sensor, sensorManager.SENSOR_DELAY_NORMAL);


    }
}
