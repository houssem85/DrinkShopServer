package com.programasoft.drinkshopserver.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.programasoft.drinkshopserver.Model.menu;

import com.programasoft.drinkshopserver.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ASUS on 20/12/2018.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {

    public MenuAdapter(Context context, List<menu> menus) {
        this.context = context;
        this.menus = menus;
    }

    private Context context;
    private List<menu> menus;


    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.menu_item_layout,null);
        return new MenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, final int position) {
        //load image
        Picasso.with(context).load(menus.get(position).getLink()).into(holder.image);
        //load text
        holder.name.setText(menus.get(position).getName());
        //events

    }

    @Override
    public int getItemCount() {
        return menus.size();
    }
}
