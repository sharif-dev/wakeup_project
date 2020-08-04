package com.example.wakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static android.content.Context.MODE_PRIVATE;

public class AlertReceiver extends BroadcastReceiver {

    private SharedPreferences prefs;
    private String alarmType;


    @Override
    public void onReceive(Context context, Intent intent) {

        prefs = context.getSharedPreferences("alarmInfo", Context.MODE_PRIVATE);
        alarmType = prefs.getString("alarmType", "");

        switch (alarmType){
            case "bird":{
                Intent intent1 = new Intent();
                intent1.setClassName(context.getPackageName(), birdActivity.class.getName());
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                break;
            }
            case "math":{
//TODO
            }
            case "shake":{
//Todo
            }
            case "walk":{
                Intent intent1 = new Intent();
                intent1.setClassName(context.getPackageName(), walkActivity.class.getName());
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                break;
            }
            case "type":{
                Intent intent1 = new Intent();
                intent1.setClassName(context.getPackageName(), TypeActivity.class.getName());
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                break;
            }
        }

    }
}