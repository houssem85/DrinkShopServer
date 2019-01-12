package com.programasoft.drinkshopserver.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.programasoft.drinkshopserver.R;

public class DrinkViewHolder extends RecyclerView.ViewHolder {

 public ImageView image;
 public TextView name;
 public TextView price;


    public DrinkViewHolder(View itemView) {
        super(itemView);
        image=(ImageView)itemView.findViewById(R.id.image);
        name=(TextView)itemView.findViewById(R.id.name);
        price=(TextView)itemView.findViewById(R.id.price);
    }
}
