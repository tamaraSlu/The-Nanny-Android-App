package com.example.thenanny;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thenanny.dto.NannyDetails;

import java.util.ArrayList;

public class ParentNannyCardAdapter extends RecyclerView.Adapter<ParentNannyCardAdapter.MyViewHolder>{

    ArrayList<NannyDetails> nannyList;

    public ParentNannyCardAdapter(ArrayList<NannyDetails> nannyList){
        this.nannyList=nannyList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView mNannyProfileImg;
        TextView mNannyName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mNannyProfileImg=itemView.findViewById(R.id.nanny_image);
            mNannyName=itemView.findViewById(R.id.nanny_name);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.nanny_card_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NannyDetails nanny=nannyList.get(position);
        Bitmap bmp = BitmapFactory.decodeByteArray(nanny.getProfile_image(), 0, nanny.getProfile_image().length);
        String fullName=(nanny.getFirstname()+" "+nanny.getLastname());
        holder.mNannyName.setText(fullName);
        holder.mNannyProfileImg.setImageBitmap(Bitmap.createScaledBitmap(bmp,100,100,false));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
