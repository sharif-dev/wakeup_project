package com.example.wakeup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class gameActivity extends AppCompatActivity {

    ListView list;

    String[] maintitle = {
            "Bird Game", "Math Game"
    };

    String[] subtitle = {
            "Tap Birds!",
            "Which one in correct?",
    };

    Integer[] imgid = {
            R.drawable.bird,
            R.drawable.math,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        MyListAdapter adapter2 = new MyListAdapter(this, maintitle, subtitle, imgid);
        list = (ListView) findViewById(R.id.gamelist);

        list.setAdapter(adapter2);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent myIntent = new Intent(getApplicationContext(), SettingActivity.class);
                    myIntent.putExtra("type","bird");
                    startActivity(myIntent);
                } else if (position == 1) {
                    Intent myIntent = new Intent(getApplicationContext(), SettingActivity.class);
                    myIntent.putExtra("type","math");
                    startActivity(myIntent);
                }
    }
});
    }}
