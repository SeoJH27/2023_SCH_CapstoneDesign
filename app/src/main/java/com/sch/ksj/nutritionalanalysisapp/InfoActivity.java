package com.sch.ksj.nutritionalanalysisapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.sch.ksj.nutritionalanalysisapp.info;

public class InfoActivity extends MainActivity{
    ImageButton imageButtonButton;
    TextView name, age, gender, weight;
    Button btnsetting;
    info info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);
        name = (TextView) findViewById(R.id.name);
        age = (TextView) findViewById(R.id.age);
        gender = (TextView) findViewById(R.id.gender);
        weight = (TextView) findViewById(R.id.weight);
        info = new info();

        name.setText("이름:\t\t" + info.getInstance().getName());

        age.setText("나이:\t\t" + info.getInstance().getAge());

        if(info.getInstance().getGender() == false)
            gender.setText("성별:\t\t여성");
        else
            gender.setText("성별:\t\t남성");

        weight.setText("몸무게:\t\t" + info.getInstance().getWeight());
        btnsetting = (Button) findViewById(R.id.btnsetting);
        btnsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InfoActivity.this, InfoSettingActivity.class));
            }
        });


        //홈버튼
        imageButtonButton = (ImageButton) findViewById(R.id.home);
        imageButtonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InfoActivity.this, MainActivity.class));
            }
        });
    }

}
