package com.example.thenanny;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    Context context;
    ArrayList<Nanny> nannyArrayList;

    public Adapter(Context context, ArrayList<Nanny> nannyArrayList) {
        this.context = context;
        this.nannyArrayList = nannyArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.card_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Nanny nanny=nannyArrayList.get(position);
        String fullName=(nanny.firstname+" "+nanny.lastname);
        holder.item_title.setText(fullName);
        holder.item_subtitle.setText(nanny.ID);

    }

    @Override
    public int getItemCount() {
        return nannyArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView item_title,item_subtitle ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_title=itemView.findViewById(R.id.item_title);
            item_subtitle=itemView.findViewById(R.id.item_subtitle);

        }
    }
}
