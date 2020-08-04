package com.example.wakeup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private String type;
    private TextView timeView;
    private TextView timeItselfView;
    private Button changBtn;
    private Button cancelBtn;
    private Button back;
    private  Boolean timeSet;
    private SeekBar seekBar;
    private TextView seekText;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        type = getIntent().getStringExtra("type");
        timeView = (TextView) findViewById(R.id.timeText);
        timeItselfView = (TextView) findViewById(R.id.time);
        changBtn = (Button) findViewById(R.id.changeBtn);
        cancelBtn = (Button) findViewById(R.id.cancleBtn);
        back = (Button) findViewById(R.id.back);
        ImageView imageView = (ImageView) findViewById(R.id.image);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekText = (TextView) findViewById(R.id.seekText);
        prefs = getSharedPreferences("alarmInfo", MODE_PRIVATE);
        editor = getSharedPreferences("alarmInfo", MODE_PRIVATE).edit();

        //Get possible past information
        int hour = prefs.getInt("hour", -1);
        int minute = prefs.getInt("minute", -1);
        boolean isSet = prefs.getBoolean("isAnyAlarmSet", false);
        editor.putInt("difficulty", seekBar.getProgress());
        editor.apply();

        switch (type){
            case "bird":{
                imageView.setImageResource(R.drawable.bird);
                break;
            }
            case "math":{
                imageView.setImageResource(R.drawable.math);
                break;
            }
            case "shake":{
                imageView.setImageResource(R.drawable.shake);
                break;
            }
            case "walk":{
                imageView.setImageResource(R.drawable.walk);
                break;
            }
            case "type":{
                imageView.setImageResource(R.drawable.type);
                break;
            }
        }


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editor.putInt("difficulty", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startIntent);

            }
        });



        //If there is already an alarm set
        if (isSet == true){

            timeView.setText("Alarm set for:");
            timeItselfView.setText(String.format("%02d", hour)  + " : " + String.format("%02d", minute));
            changBtn.setText("RESET TIME");
            seekBar.setVisibility(View.VISIBLE);
            seekText.setVisibility(View.VISIBLE);
            back.setVisibility(View.INVISIBLE);

        }
        //If there is no alarm
        else {
            timeView.setText("No alarm set");
            timeItselfView.setText("");
            cancelBtn.setVisibility(View.INVISIBLE);
            seekBar.setVisibility(View.INVISIBLE);
            seekText.setVisibility(View.INVISIBLE);
            changBtn.setText("PICK A TIME");
            back.setVisibility(View.VISIBLE);

        }

        //changing the alarm time
        changBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(), "Time Picker");

            }
        });

        //canceling the alarm
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }

        });
    }

    //when a new alarm time is set
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        timeView.setText("Alarm set for:");
        timeItselfView.setText(String.format("%02d", hourOfDay)  + " : " + String.format("%02d", minute));
        changBtn.setText("RESET TIME");
        cancelBtn.setVisibility(View.VISIBLE);

        seekBar.setVisibility(View.VISIBLE);
        seekText.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);

        editor.putBoolean("isAnyAlarmSet", true);
        editor.putString("alarmType",type);
        editor.putInt("hour", hourOfDay);
        editor.putInt("minute", minute);
        editor.apply();

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        newCalendar.set(Calendar.MINUTE, minute);
        newCalendar.set(Calendar.SECOND, 0);

        createAlarm(newCalendar);



    }

    //creating the alarm for chosen time
    private void createAlarm(Calendar calendar){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, myIntent, 0);

        alarmManager.cancel(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT).show();

    }

    //canceling the alarm
    private void cancelAlarm(){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, myIntent, 0);

        editor.putBoolean("isAnyAlarmSet", false);
        editor.putString("alarmType","");
        editor.putInt("hour", -1);
        editor.putInt("minute", -1);
        editor.apply();

        alarmManager.cancel(pendingIntent);

        timeView.setText("No alarm set");
        timeItselfView.setText("");
        cancelBtn.setVisibility(View.INVISIBLE);
        seekBar.setVisibility(View.INVISIBLE);
        seekText.setVisibility(View.INVISIBLE);
        changBtn.setText("PICK A TIME");
        back.setVisibility(View.VISIBLE);

        Toast.makeText(this, "Alarm canceled", Toast.LENGTH_SHORT).show();

    }

}
