package com.example.wakeup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TypeActivity extends AppCompatActivity{


    private Integer errors = 0;
    private Integer allowedErrors;
    private TextView errorView;
    private EditText inputEditText;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private String sample = "I promise to wake up now and wash my face and eat my breakfast and"+
            " do not sleep again";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        editor = getSharedPreferences("alarmInfo", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("alarmInfo", MODE_PRIVATE);
        allowedErrors = 10 -  prefs.getInt("difficulty", 3);

        TextView textView = (TextView) findViewById(R.id.sample);
        textView.setText(sample);
        inputEditText = (EditText) findViewById(R.id.inputText);
        errorView = (TextView) findViewById(R.id.error);
        errorView.setText(allowedErrors.toString());


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

        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] inputArray = inputEditText.getText().toString().split(" ");
                String[] sampleArray = sample.split(" ");
                int min = Math.min(inputArray.length, sampleArray.length);
                for (int count = 0; count < min ; count++){
                    if (inputArray[count].equals(sampleArray[count]))
                        continue;
                    errors ++;
                }

                errors += Math.abs(sampleArray.length - inputArray.length);

                if (errors <= allowedErrors){
                    vibrator.cancel();
                    r.stop();
                    editor.putBoolean("isAnyAlarmSet", false);
                    editor.putString("alarmType", "");
                    editor.apply();
                    Intent startIntent = new Intent(getApplicationContext(), SettingActivity.class);
                    startIntent.putExtra("type", "type");
                    startActivity(startIntent);
                }
                else {
                    TextView errorView = (TextView) findViewById(R.id.tryAgain);
                    errorView.setVisibility(View.VISIBLE);
                    inputEditText.setText("");
                    errors = 0;
                }
            }
        });






    }
}
