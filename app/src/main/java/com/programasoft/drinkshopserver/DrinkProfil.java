package com.programasoft.drinkshopserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.programasoft.drinkshopserver.Adapter.MenuAdapter;
import com.programasoft.drinkshopserver.Model.drink;
import com.programasoft.drinkshopserver.Model.error;
import com.programasoft.drinkshopserver.Model.menu;
import com.programasoft.drinkshopserver.Retrofit.IDrinkShopApi;
import com.programasoft.drinkshopserver.Utils.Comment;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinkProfil extends AppCompatActivity {
private ImageView image;
private drink drink;
private EditText txt_name;
private EditText txt_price;
private IDrinkShopApi api;
private menu menu;
private MaterialSpinner spinner;
private List<menu> Menus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_profil);
        api=Comment.getApi();
        image=(ImageView)this.findViewById(R.id.image_drink);
        drink=(drink)this.getIntent().getSerializableExtra("drink");
        menu=(menu)this.getIntent().getSerializableExtra("menu");
        txt_name=(EditText)this.findViewById(R.id.txt_name);
        txt_price=(EditText)this.findViewById(R.id.txt_price);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        api.GetMenu().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<menu>>() {
            @Override
            public void accept(List<menu> menus) throws Exception {
                Menus = menus;
                spinner.setItems(Menus);
                for(int i=0;i<Menus.size();i++)
                {  if(Menus.get(i).getID()==menu.getID())
                   {spinner.setSelectedIndex(i);

                   }

                }
            }
        });


        Picasso.with(this).load(drink.getLink()).into(image);
        txt_name.setText(drink.getName());
        txt_price.setText(drink.getPrice()+"");
    }

    public void update(View view) {


         if(txt_price.getText().toString().matches("[+-]?((\\d+\\.?\\d*)|(\\.\\d+))"))
         {   if(txt_name.getText().toString().isEmpty()==false) {
             final android.app.AlertDialog  progressDialog=new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
             progressDialog.show();
             drink.setName(txt_name.getText().toString());
             drink.setPrice(Double.valueOf(txt_price.getText().toString()));
             drink.setMenu_id(Menus.get(spinner.getSelectedIndex()).getID());
             api.UpdateDrink(drink.getId(),drink.getName(),drink.getPrice(),drink.getMenu_id()).enqueue(new Callback<Boolean>() {
                 @Override
                 public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                 if(response.isSuccessful())
                 {  progressDialog.dismiss();
                     if(response.body())
                    {    Intent i=new Intent(getApplicationContext(),Drink_layout.class);
                         DrinkProfil.this.finish();
                         i.putExtra("menu",menu);
                         DrinkProfil.this.startActivity(i);

                    }else
                    {Toast.makeText(getApplicationContext(),"Try again",Toast.LENGTH_SHORT).show();

                    }

                 }else
                     {   progressDialog.dismiss();
                         try {
                             Gson gson = new GsonBuilder().setLenient().create();
                             error error = gson.fromJson(response.errorBody().string(), error.class);
                             Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                         } catch (IOException e) {
                             e.printStackTrace();
                         }

                     }
                     }

                 @Override
                 public void onFailure(Call<Boolean> call, Throwable t) {
                 Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                 }
             });


             }else {
                    Toast.makeText(this,"Enter name",Toast.LENGTH_SHORT).show();
             }
         }else {
             Toast.makeText(this,"Enter Valid price",Toast.LENGTH_SHORT).show();
         }
    }

    public void delete(View view) {
           final android.app.AlertDialog  progressDialog=new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
            progressDialog.show();
    api.DeleteDrink(drink.getId()).enqueue(new Callback<Boolean>() {
        @Override
        public void onResponse(Call<Boolean> call, Response<Boolean> response) {

            if(response.isSuccessful())
            {   progressDialog.dismiss();
                if(response.body()==true)
               {
                  Intent i=new Intent(getApplicationContext(),Drink_layout.class);
                  DrinkProfil.this.finish();
                  i.putExtra("menu",menu);
                   DrinkProfil.this.startActivity(i);
               }else
               {
                   Toast.makeText(getApplicationContext(), "Faild try again", Toast.LENGTH_SHORT).show();
               }

        }else
            {  progressDialog.dismiss();
                try {
                    Gson gson = new GsonBuilder().setLenient().create();
                    error error = gson.fromJson(response.errorBody().string(), error.class);
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onFailure(Call<Boolean> call, Throwable t) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
        }
    });

    }
}
