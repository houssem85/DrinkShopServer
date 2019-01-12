package com.programasoft.drinkshopserver.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.programasoft.drinkshopserver.Model.drink;
import com.programasoft.drinkshopserver.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkViewHolder> {

    private List<drink> list_drink;
    private Context context;

    public DrinkAdapter(List<drink> list_drink, Context context) {
        this.list_drink = list_drink;
        this.context = context;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.drink_item_layout,null);
        DrinkViewHolder holder=new DrinkViewHolder(view);
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
