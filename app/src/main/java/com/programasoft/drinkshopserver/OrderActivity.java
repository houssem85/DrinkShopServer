package com.programasoft.drinkshopserver;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.programasoft.drinkshopserver.Adapter.OrderAdapter;
import com.programasoft.drinkshopserver.Model.error;
import com.programasoft.drinkshopserver.Model.order;
import com.programasoft.drinkshopserver.R;
import com.programasoft.drinkshopserver.Retrofit.IDrinkShopApi;
import com.programasoft.drinkshopserver.Utils.Comment;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView list_orders;
    private OrderAdapter adapter;
    private IDrinkShopApi api;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        list_orders=(RecyclerView)this.findViewById(R.id.list_orders);
        list_orders.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        api=Comment.getApi();
        bottomNavigationView=(BottomNavigationView)this.findViewById(R.id.button_navication);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.NewOrders)
                {loadOreders(0);

                }else if(item.getItemId()==R.id.CanceldOrders)
                {loadOreders(1);

                }
                else if(item.getItemId()==R.id.ProcessingOrders)
                {loadOreders(2);

                }else if(item.getItemId()==R.id.ShippingOrders)
                {loadOreders(3);

                }
                else if(item.getItemId()==R.id.ShippdedOrders)
                {loadOreders(4);

                }
                return true;
            }
        });
        loadOreders(0);
    }


    private void  loadOreders(int status)
    {api.GetOrdersByStatus(status).enqueue(new Callback<List<order>>() {
        @Override
        public void onResponse(Call<List<order>> call, Response<List<order>> response) {
            if(response.isSuccessful())
            {   adapter=new OrderAdapter(OrderActivity.this,response.body());
                list_orders.setAdapter(adapter);

            }else
            { try {
                Gson gson = new GsonBuilder().setLenient().create();
                error error = gson.fromJson(response.errorBody().string(), error.class);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            }

        }

        @Override
        public void onFailure(Call<List<order>> call, Throwable t) {
            Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
        }
    });

    }
    }

