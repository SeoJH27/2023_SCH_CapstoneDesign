package com.sch.ksj.nutritionalanalysisapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ListActivity extends MainActivity implements MAdapter.ItemClickListener{
    ImageButton home;
    MAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //홈버튼
        home = (ImageButton) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListActivity.this, MainActivity.class));
            }
        });

        ArrayList<String> data = new ArrayList<>();
        //데이터 생성


        RecyclerView rcycl = findViewById(R.id.recycler);
        rcycl.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new MAdapter(this, data);
        adapter.setClickLister(this);
        rcycl.setAdapter(adapter);
    }

    @Override
    public void onItemClick (View view, int position){

    }
}
