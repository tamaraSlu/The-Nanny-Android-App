package com.example.thenanny;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.thenanny.dto.NannyDetails;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    Context context;
    ArrayList<NannyDetails> nannyArrayList;

    public Adapter(Context context, ArrayList<NannyDetails> nannyArrayList) {
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
        NannyDetails nanny=nannyArrayList.get(position);
        String fullName=(nanny.getFirstname()+" "+nanny.getLastname());
        holder.item_title.setText(fullName);
        holder.item_subtitle.setText(nanny.getId());

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
