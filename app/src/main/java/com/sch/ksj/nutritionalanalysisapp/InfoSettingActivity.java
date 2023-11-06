package com.sch.ksj.nutritionalanalysisapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import com.sch.ksj.nutritionalanalysisapp.info;
import android.widget.RadioGroup;
import android.widget.Toast;

public class InfoSettingActivity extends MainActivity {
    Toast toast;

    ImageButton imageButton;
    Button btnsave;
    info info;
    EditText name, age, weight;
    RadioButton gender_female, gender_male;

    boolean gender1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info_setting);

        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        weight = (EditText) findViewById(R.id.weight);
        gender_female = (RadioButton) findViewById(R.id.gender_female);
        gender_male = (RadioButton) findViewById(R.id.gender_male);
        info = new info();

        gender_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender1= false;
            }
        });
        gender_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender1 = true;
            }
        });

        //홈버튼
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InfoSettingActivity.this, MainActivity.class));
            }
        });


        //저장
        btnsave = (Button) findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString() == null || !isInteger(age.getText().toString()) || !isInteger(weight.getText().toString()))
                {
                    toast.makeText(InfoSettingActivity.this.getApplicationContext(),"다시 입력해주세요!", Toast.LENGTH_SHORT);
                }
                else{
                    info.getInstance().setName(name.getText().toString());
                    info.getInstance().setAge(Integer.parseInt(age.getText().toString()));
                    info.getInstance().setGender(gender1);
                    info.getInstance().setWeight(Integer.parseInt(weight.getText().toString()));
                    startActivity(new Intent(InfoSettingActivity.this, MainActivity.class));
                }
            }
        });
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
