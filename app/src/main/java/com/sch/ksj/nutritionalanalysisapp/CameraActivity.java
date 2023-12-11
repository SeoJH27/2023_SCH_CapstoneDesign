package com.sch.ksj.nutritionalanalysisapp;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import java.io.File;

public class CameraActivity extends MainActivity{
    ImageButton home;
    Button camera;
    ImageView image;
    File storge;
    Dialog dialog;
    Uri photoURI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);
        storge = getFilesDir();
        //홈버튼
        home = (ImageButton) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CameraActivity.this, MainActivity.class));
            }
        });

        camera = (Button) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File photoFile = null;
                File tempImage = new File(storge, "image.jpg");
                photoFile = tempImage;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoURI = FileProvider.getUriForFile(CameraActivity.this, getPackageName()+".fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            // 데이터 저장
            btnClick();
        }
        else Toast.makeText(this, "오류!", Toast.LENGTH_SHORT).show();
    }

    public void btnClick(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_form);
        dialog.setTitle("이미지를 확인하세요!");
        ImageView img = (ImageView) dialog.findViewById(R.id.pic);
        img.setImageURI(photoURI);
        Button pButton = (Button) dialog.findViewById(R.id.positive);
        Button nButton = (Button) dialog.findViewById(R.id.neutral);

        dialog.show();

        // Yes 버튼 및 이벤트 생성
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CameraActivity.this, OCRActivity.class));
            }
        });

        //No 버튼 및 이벤트 생성
        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pass
                dialog.cancel();
            }
        });

    }
}
