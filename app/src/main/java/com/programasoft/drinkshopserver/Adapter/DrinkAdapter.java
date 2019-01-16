package com.programasoft.drinkshopserver.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.programasoft.drinkshopserver.Model.menu;
import com.programasoft.drinkshopserver.DrinkProfil;
import com.programasoft.drinkshopserver.Model.drink;
import com.programasoft.drinkshopserver.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkViewHolder> {

    private List<drink> list_drink;
    private Context context;
    private menu menu;

    public DrinkAdapter(List<drink> list_drink, Context context, com.programasoft.drinkshopserver.Model.menu menu) {
        this.list_drink = list_drink;
        this.context = context;
        this.menu = menu;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.drink_item_layout,null);
        final DrinkViewHolder holder=new DrinkViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                drink drink=list_drink.get(position);
                Intent i=new Intent(context,DrinkProfil.class);
                i.putExtra("drink",drink);
                i.putExtra("menu",menu);
                context.startActivity(i);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
        drink drink=list_drink.get(position);
        Picasso.with(context).load(drink.getLink()).into(holder.image);
        holder.name.setText(drink.getName());
        holder.price.setText(drink.getPrice()+" dt");

    }

    @Override
    public int getItemCount() {
       return  list_drink.size();
    }
}
