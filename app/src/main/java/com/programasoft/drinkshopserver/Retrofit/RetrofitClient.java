package com.programasoft.drinkshopserver.Retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.programasoft.drinkshopserver.Utils.Comment;
import com.programasoft.drinkshopserver.offline.InternetConnectionListener;
import com.programasoft.drinkshopserver.offline.NetworkConnectionInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by ASUS on 27/12/2018.
 */

public class RetrofitClient {


    private static Retrofit retrofit=null;

    public static Retrofit getClient()
    {   if(retrofit==null) {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();

        okhttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String username="houssem";
                String passworld="123456789";
                String base=username+":"+passworld;
                String cle="Basic "+Base64.encodeToString(base.getBytes(),Base64.NO_WRAP);
                Request request=chain.request();
                Request.Builder builder= request.newBuilder().header("Authorization",cle);
                return chain.proceed(builder.build());
            }
        });


        okhttpClientBuilder.connectTimeout(60, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(60, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(60, TimeUnit.SECONDS);
        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Comment.SERVER_URL).addConverterFactory(GsonConverterFactory.create(gson)).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(okhttpClientBuilder.build()).addConverterFactory(ScalarsConverterFactory.create());;
        retrofit=builder.build();

    }
        return retrofit;
    }
}
