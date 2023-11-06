package com.sch.ksj.nutritionalanalysisapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MAdapter extends RecyclerView.Adapter<MAdapter.ViewHolder> {
    private ArrayList<String> data;
    private LayoutInflater inflater;
    private ItemClickListener mClickListener;

    MAdapter(Context context, ArrayList<String> data){
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int viewType){
        View view = inflater.inflate(R.layout.item, group, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.tx.setText(data.get(position));
    }

    @Override
    public int getItemCount(){
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Button tx;

        ViewHolder(View itemView){
            super(itemView);
            tx = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            if(mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    String getItem(int id){
        return data.get(id);
    }

    void setClickLister(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
}
