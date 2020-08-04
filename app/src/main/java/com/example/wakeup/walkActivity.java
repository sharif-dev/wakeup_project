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
import android.widget.TextView;

public class walkActivity extends AppCompatActivity{

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener stepDetector;
    private Integer stepCount = 0;
    private TextView textView;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);
        editor = getSharedPreferences("alarmInfo", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("alarmInfo", MODE_PRIVATE);
        stepCount =  prefs.getInt("difficulty", 3);



        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        textView = (TextView) findViewById(R.id.remainigSteps);
        textView.setText(stepCount.toString());


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



        stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if (event!= null) {
                    stepCount--;
                    textView.setText(stepCount.toString());
                    if (stepCount == 0){
                        vibrator.cancel();
                        r.stop();
                        sensorManager.unregisterListener(stepDetector);
                        editor.putBoolean("isAnyAlarmSet", false);
                        editor.putString("alarmType","");
                        editor.apply();
                        Intent startIntent = new Intent(getApplicationContext(), SettingActivity.class);
                        startIntent.putExtra("type","walk");
                        startActivity(startIntent);
                    }
                }
                }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

        };
        sensorManager.registerListener(stepDetector, sensor, sensorManager.SENSOR_DELAY_NORMAL);

    }
}
