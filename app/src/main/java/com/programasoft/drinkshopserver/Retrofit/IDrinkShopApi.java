package com.programasoft.drinkshopserver.Retrofit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import com.programasoft.drinkshopserver.Model.menu;

import java.util.List;

/**
 * Created by ASUS on 27/12/2018.
 */

public interface IDrinkShopApi {

    @GET("GetMenu.php")
    Observable<List<menu>> GetMenu();


    @Multipart
    @POST("UploadMenuImage.php")
    Call<String> UploadMenuImage(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("InsertNewMenu.php")
    Call<String> InsertNewMenu(@Field("Name") String Name,@Field("Link") String Link);
}
