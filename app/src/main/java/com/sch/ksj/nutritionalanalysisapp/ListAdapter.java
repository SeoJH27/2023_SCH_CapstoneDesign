package com.sch.ksj.nutritionalanalysisapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
    ArrayList<String[]> data = new ArrayList<>();
    static Dialog dialog;
    Context context;

    public ListAdapter (Context context, ArrayList<String[]> dataSet) {
        this.context =context;
        data = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView, textView1;
        private ImageView ImageView;
        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            DBProc DBProc = new DBProc(context);
            ArrayList<String[]> data = new ArrayList<>();
            data = DBProc.selectALL();

            textView = itemView.findViewById(R.id.list_text);
            textView1 = itemView.findViewById(R.id.num);
            ImageView = itemView.findViewById(R.id.list_pic);

            try {
                //리스트 아이템 클릭
                ArrayList<String[]> finalData = data;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //데이터베이스에서 데이터 불러오기
                        //분석 내용
                        int num = Integer.parseInt(textView1.getText().toString()) - 1;
                        System.out.println(num);
                        String text = finalData.get(num)[3];
                        System.out.println(finalData.get(num)[1]);
                        dialog = new Dialog(context);
                        dialog.setContentView(R.layout.list_dialog);
                        dialog.setTitle("날짜: " + textView.toString());
                        ImageView img = (ImageView) dialog.findViewById(R.id.pic_detail);
                        TextView txt = (TextView) dialog.findViewById(R.id.text_detail);
                        Button cButton = (Button) dialog.findViewById(R.id.list_close);

                        Bitmap image = StringToBitmap(finalData.get(num)[2]);
                        img.setImageBitmap(image);
                        txt.setText(text);

                        dialog.show();

                        //close 버튼
                        cButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                    }
                });
            }
            catch (Exception e){
                Log.e("Error: ListAdaptor", e.toString());
            }
        }
    }

    // ViewHolder 객체를 생성하여 리턴한다.
    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        ListAdapter.ViewHolder viewHolder = new ListAdapter.ViewHolder(view, context);
        return viewHolder;
    }

    // ViewHolder안의 내용을 position에 해당되는 데이터로 교체한다.
    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        String num = data.get(position)[0];
        String date = data.get(position)[1];
        String pic = data.get(position)[2];
        Bitmap Pic = StringToBitmap(pic);
        holder.textView.setText(date);
        holder.textView1.setText(num);
        holder.ImageView.setImageBitmap(Pic);
    }

    // 전체 데이터의 갯수를 리턴한다.
    @Override
    public int getItemCount() {
        if(data != null)
            return data.size();
        return 0;
    }

    //String형을 BitMap으로 변환시켜주는 함수
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
