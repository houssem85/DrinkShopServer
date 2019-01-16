package com.programasoft.drinkshopserver.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.programasoft.drinkshopserver.Model.order;
import com.programasoft.drinkshopserver.OrderPlaceActivity;
import com.programasoft.drinkshopserver.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    private Context context;
    private List<order> orderList;

    public OrderAdapter(Context context, List<order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.order_item_layout,parent,false);
        OrderViewHolder holder=new OrderViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, int position) {
        final order order=orderList.get(position);
        holder.txt_id.setText("#"+order.getId());
        holder.txt_price.setText(order.getPrice()+" dt");
        holder.txt_comment.setText("Comment :"+order.getComment());
        holder.name.setText(order.getName());
        Picasso.with(context).load(order.getLink()).into(holder.image);
        holder.price.setText(order.getPrice()+" dt");
        holder.description.setText("ice:"+order.getIce()+"% sugar:"+order.getSugar()+"%");
        holder.txt_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.view_forground.getVisibility()==View.GONE)
                holder.view_forground.setVisibility(View.VISIBLE);
                else
                {holder.view_forground.setVisibility(View.GONE);

                }
            }
        });
        holder.phone_user.setText("Phone uers:"+order.getId_user());
        holder.img_locatisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             double latitude;
             double longitude;
             String address=order.getAddress();
             String[] adrresss = address.split(",");
             latitude=Double.valueOf(adrresss[0]);
             longitude=Double.valueOf(adrresss[1]);
                Intent i=new Intent(context,OrderPlaceActivity.class);
                i.putExtra("latitude",latitude);
                i.putExtra("longitude",longitude);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
