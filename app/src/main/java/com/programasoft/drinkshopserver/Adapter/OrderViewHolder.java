package com.programasoft.drinkshopserver.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.programasoft.drinkshopserver.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {
    public TextView txt_id;
    public TextView txt_price;
    public TextView txt_comment;
    public TextView name;
    public TextView description;
    public TextView price;
    public ImageView image;
    public LinearLayout view_forground;
    public TextView txt_details;
    public TextView phone_user;
    public ImageView img_locatisation;

    public OrderViewHolder(View itemView) {
        super(itemView);
        txt_id=(TextView)itemView.findViewById(R.id.id);
        txt_price=(TextView)itemView.findViewById(R.id.txt_price);
        phone_user=(TextView)itemView.findViewById(R.id.txt_phone_user);
        txt_comment=(TextView)itemView.findViewById(R.id.txt_comment);
        name=(TextView) itemView.findViewById(R.id.name);
        description=(TextView) itemView.findViewById(R.id.description);
        price=(TextView) itemView.findViewById(R.id.price);
        image=(ImageView)itemView.findViewById(R.id.image);
        view_forground=(LinearLayout)itemView.findViewById(R.id.View_Forground);
        txt_details=(TextView) itemView.findViewById(R.id.txt_detail);
        img_locatisation=(ImageView)itemView.findViewById(R.id.img_locatisation);
        }
}
