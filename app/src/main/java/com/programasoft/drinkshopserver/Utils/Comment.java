package com.programasoft.drinkshopserver.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.programasoft.drinkshopserver.Retrofit.IDrinkShopApi;
import com.programasoft.drinkshopserver.Retrofit.RetrofitClient;
import com.programasoft.drinkshopserver.offline.InternetConnectionListener;

/**
 * Created by ASUS on 27/12/2018.
 */

public class Comment {
    public final static String SERVER_URL="https://houssemdaou85.000webhostapp.com/DrinkShop/";


    public static IDrinkShopApi getApi()
    {
        return RetrofitClient.getClient().create(IDrinkShopApi.class);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
