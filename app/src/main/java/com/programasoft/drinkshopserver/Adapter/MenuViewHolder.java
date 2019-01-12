package com.programasoft.drinkshopserver.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.programasoft.drinkshopserver.R;


/**
 * Created by ASUS on 20/12/2018.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder  {
    public ImageView image;
    public ImageView image_more;
    public TextView name;

    public MenuViewHolder(View itemView) {
        super(itemView);
        image=(ImageView)itemView.findViewById(R.id.image);
        name=(TextView) itemView.findViewById(R.id.name);
        image_more=(ImageView)itemView.findViewById(R.id.image_more);

    }


}
