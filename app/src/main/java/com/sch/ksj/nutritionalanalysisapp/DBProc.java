package com.sch.ksj.nutritionalanalysisapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "database.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE nutDB (num INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, pic BLOB, analyzing TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersioin) {
        db.execSQL("DROP TABLE IF EXISTS nutDB");
        onCreate(db);
    }
}

public class DBProc extends MainActivity{

    DBHelper DBHelper;
    SQLiteDatabase sqlDB;

    public DBProc(Context context){
        DBHelper  = new DBHelper(context);
        sqlDB = DBHelper.getWritableDatabase();
    }

    public void insertPic(String text){
        try{
            long now = System.currentTimeMillis();
            Date dt = new Date(now);
            SimpleDateFormat date = new SimpleDateFormat("yy.MM.dd HH:mm");
            String strDate = date.format(dt);
            String base64Img = imageToBase64();
            sqlDB.execSQL("INSERT INTO nutDB (date, pic, analyzing) VALUES ('" + strDate + "', '" + base64Img + "', '" + text + "');");
            sqlDB.close();
            Log.v("db", "성공");
        }
        catch (Exception e)
        {
            Log.e("Error: DBActivity", "오류: "+ e.toString());
        }
    }

    public ArrayList<String[]> selectALL(){
        ArrayList<String[]> data = new ArrayList<>();
        Cursor cursor = sqlDB.rawQuery("SELECT num, date, pic, analyzing FROM nutDB;", null);

        while(cursor.moveToNext()){
            String[] dt = new String[]
                    {cursor.getString(0),   //num
                     cursor.getString(1),   //data
                     cursor.getString(2),   //pic
                     cursor.getString(3)};  //analyzing
            data.add(dt);
        }
        return data;
    }

    public static String imageToBase64(){
        String base64Img = "";

        File f = new File("/data/user/0/com.sch.ksj.nutritionalanalysisapp/files", "image.jpg");
        Log.d("파일", f.toString());

        if (f.exists() && f.isFile() && f.length() > 0) {
            byte[] bt = new byte[(int) f.length()];
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                fis.read(bt);
                base64Img = new String(Base64.getEncoder().encode(bt));
                Log.v("base64Img : bt", bt.toString());
            } catch (Exception e) {
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    Log.e("Error!","IOException : " + e.getMessage());
                } catch (Exception e) {
                    Log.e("Error!","Exception : " + e.getMessage());
                }
            }
        }
        Log.v("base64Img",base64Img);

        return base64Img;
    }
}
