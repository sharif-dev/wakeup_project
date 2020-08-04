package com.example.wakeup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ListView list;
    private SharedPreferences prefs;

    String[] maintitle = {
            "Game", "Shake",
            "Walk", "Type",
    };

    String[] subtitle = {
            "Play game to dismiss alarm", "Shake to dismiss alarm",
            "Walk to dismiss alarm", "Type the given text to dismiss alarm",
    };

    Integer[] imgid = {
            R.drawable.game, R.drawable.shake,
            R.drawable.walk, R.drawable.type,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("alarmInfo", MODE_PRIVATE);



        boolean isAnyAlarmSet = prefs.getBoolean("isAnyAlarmSet", false);

        if (isAnyAlarmSet) {
            String alarmType = prefs.getString("alarmType", "");
            switch (alarmType){
                case "bird":{
                    Intent myIntent = new Intent(getApplicationContext(), gameActivity.class);
                    myIntent.putExtra("type","bird");
                    startActivity(myIntent);
                    break;
                }
                case "math":{
                    Intent myIntent = new Intent(getApplicationContext(), SettingActivity.class);
                    myIntent.putExtra("type","math");
                    startActivity(myIntent);
                    break;
                }
                case "shake":{
                    Intent myIntent = new Intent(getApplicationContext(), SettingActivity.class);
                    myIntent.putExtra("type","shake");
                    startActivity(myIntent);
                    break;
                }
                case "walk":{
                    Intent myIntent = new Intent(getApplicationContext(), SettingActivity.class);
                    myIntent.putExtra("type","walk");
                    startActivity(myIntent);
                    break;
                }
                case "type":{
                    Intent myIntent = new Intent(getApplicationContext(), SettingActivity.class);
                    myIntent.putExtra("type","type");
                    startActivity(myIntent);
                    break;
                }
            }

        } else {
            MyListAdapter adapter = new MyListAdapter(this, maintitle, subtitle, imgid);
            list = (ListView) findViewById(R.id.list);
            list.setAdapter(adapter);


            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    if (position == 0) {
                        Intent myIntent = new Intent(getApplicationContext(), gameActivity.class);
                        startActivity(myIntent);

                    } else if (position == 1) {

                        Intent myIntent = new Intent(getApplicationContext(), SettingActivity.class);
                        myIntent.putExtra("type","shake");
                        startActivity(myIntent);

                    } else if (position == 2) {

                        Intent myIntent = new Intent(getApplicationContext(), SettingActivity.class);
                        myIntent.putExtra("type","walk");
                        startActivity(myIntent);

                    } else if (position == 3) {

                        Intent myIntent = new Intent(getApplicationContext(), SettingActivity.class);
                        myIntent.putExtra("type","type");
                        startActivity(myIntent);
                    }

                }
            });
        }


    }
}