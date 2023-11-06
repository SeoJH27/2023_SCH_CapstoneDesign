package com.sch.ksj.nutritionalanalysisapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends MainActivity{
    ImageButton imageButtonButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        //홈버튼
        imageButtonButton = (ImageButton) findViewById(R.id.imageButton);
        imageButtonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
            }
        });




    }
}
