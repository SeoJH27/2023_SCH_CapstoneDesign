package com.sch.ksj.nutritionalanalysisapp;
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
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import java.io.File;

public class CameraActivity extends MainActivity{
    ImageButton home;
    Button camera;
    ImageView image;
    File storge;

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
            File photoFile = null;
            @Override
            public void onClick(View view) {
                File tempImage = new File(storge, "image.jpg");
                photoFile = tempImage;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoURI = FileProvider.getUriForFile(CameraActivity.this, getPackageName()+".fileprovider", photoFile);
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

/*            image = (ImageView) findViewById(R.id.image);
            image.;*/

            // 데이터 저장
            btnClick();
/*

            try{
                tempFile.createNewFile();
                FileOutputStream out = new FileOutputStream(tempFile);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.close();
                btnClick();
            }catch (FileNotFoundException e) {
                Log.e("Error!","FileNotFoundException : " + e.getMessage());
            } catch (IOException e) {
                Log.e("Error!","IOException : " + e.getMessage());
            }
*/
        }
        else Toast.makeText(this, "오류!", Toast.LENGTH_SHORT).show();
    }

    public void btnClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이미지를 확인하세요!");
        builder.setMessage("분석을 진행할까요?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // Yes 버튼 및 이벤트 생성
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(CameraActivity.this, OCRActivity.class));
            }
        });

        //No 버튼 및 이벤트 생성
        builder.setNeutralButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Pass
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
