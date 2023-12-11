package com.sch.ksj.nutritionalanalysisapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListActivity extends MainActivity {
    ImageButton home;
    SQLiteDatabase db;
    Dialog dialog;
    ArrayList<String[]> data = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        DBProc DBProc = new DBProc(ListActivity.this);

        //홈버튼
        home = (ImageButton) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListActivity.this, MainActivity.class));
            }
        });

        try{
            //데이터 가져오기
            data = DBProc.selectALL();

            RecyclerView recyclerView = findViewById(R.id.recycler);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Context) this);
            recyclerView.setLayoutManager(linearLayoutManager);  // LayoutManager 설정

            ListAdapter listAdapter = new ListAdapter(this, data);
            recyclerView.setAdapter(listAdapter); // 어댑터 설정

            System.out.println("data");
        }
        catch(Exception e){
            Log.e("Error: ListActivity", e.toString());
        }
    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            System.out.println("StringToBitmap");
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
