package com.sch.ksj.nutritionalanalysisapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{
    public SharedPreferences prefs;
    Button nuturient, note, personalInfo, setting;
    info info;
    String name;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("Pref", MODE_PRIVATE);
        checkFirstRun();
        setContentView(R.layout.activity_main);

        info = new info();
        TextView textview = (TextView) findViewById(R.id.textview);

        if(info.getInstance().getName() != null && info.getInstance().getName() != "") {
            name = info.getInstance().getName();
            textview.setText(name + "님 환영합니다.");
        }

        nuturient = (Button) findViewById(R.id.nuturient);
        note = (Button) findViewById(R.id.note);
        personalInfo = (Button) findViewById(R.id.personalInfo);
        setting = (Button) findViewById(R.id.setting);

        nuturient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
            }
        });

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RecodingActivity.class));
            }
        });

        personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InfoActivity.class));
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

    }
    public void checkFirstRun(){
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);
        if(isFirstRun){
            Intent intent = new Intent(this, InfoSettingActivity.class);
            startActivity(intent);
            prefs.edit().putBoolean("isFirstRun", false).apply();
        }
    }
}
