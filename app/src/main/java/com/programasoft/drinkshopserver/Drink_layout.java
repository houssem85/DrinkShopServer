package com.programasoft.drinkshopserver;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.programasoft.drinkshopserver.Adapter.DrinkAdapter;
import com.programasoft.drinkshopserver.Model.drink;
import com.programasoft.drinkshopserver.Model.error;
import com.programasoft.drinkshopserver.Model.menu;
import com.programasoft.drinkshopserver.Retrofit.IDrinkShopApi;
import com.programasoft.drinkshopserver.Utils.Comment;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Drink_layout extends AppCompatActivity {

    private static final int PICK_IMAGE = 500;
    private menu menu;
    private RecyclerView recyclerView;
    private IDrinkShopApi api;
    private DrinkAdapter adapter;
    private TextView toolbare_title;
    private ImageView image;
    private File uploaded_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_layout);
        menu=(menu)this.getIntent().getSerializableExtra("menu");
        recyclerView=(RecyclerView)this.findViewById(R.id.list_drink);
        toolbare_title=(TextView)this.findViewById(R.id.toolbar_title);
        toolbare_title.setText(menu.getName());
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        api=Comment.getApi();
        load_data();
    }

    public void addDrink(View view) {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View v=LayoutInflater.from(this).inflate(R.layout.add_drink_layout,null);
        image=(ImageView)v.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        final EditText txt_name=(EditText)v.findViewById(R.id.txt_name);
        final EditText txt_price=(EditText)v.findViewById(R.id.txt_price);

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
               if(txt_name.getText().toString().isEmpty()==false && txt_price.getText().toString().isEmpty()==false)
               {  String name=txt_name.getText().toString();

                   if(txt_price.getText().toString().matches("[+-]?((\\d+\\.?\\d*)|(\\.\\d+))"))
                   {   double price=Double.valueOf(txt_price.getText().toString());
                       if(uploaded_file!=null)
                       {   final android.app.AlertDialog  progressDialog=new SpotsDialog.Builder().setContext(Drink_layout.this).setCancelable(false).build();
                           progressDialog.show();
                           String link=Comment.SERVER_URL+"drink_img/"+menu.getID()+"_"+name+".jpg";
                           int menu_id=menu.getID();
                           RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), uploaded_file);
                           RequestBody request_name = RequestBody.create(MediaType.parse("multipart/form-data"),name);
                           RequestBody request_link = RequestBody.create(MediaType.parse("multipart/form-data"),link);
                           RequestBody request_price = RequestBody.create(MediaType.parse("multipart/form-data"),price+"");
                           RequestBody request_menu_id = RequestBody.create(MediaType.parse("multipart/form-data"),menu_id+"");
                           MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", menu.getID()+"_"+name+".jpg",requestFile);

                           api.AddDrink(body,request_name,request_link,request_price,request_menu_id).enqueue(new Callback<Boolean>() {
                               @Override
                               public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                   progressDialog.dismiss();
                                   if(response.isSuccessful())
                                  {  if(response.body()==true)
                                     { load_data();

                                     }else
                                     {
                                         Toast.makeText(getApplicationContext(), "Faild try again", Toast.LENGTH_SHORT).show();
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

                       }else
                       {
                           Toast.makeText(getApplicationContext(),"Chose Image",Toast.LENGTH_SHORT).show();
                       }

                   }else
                   {
                       Toast.makeText(getApplicationContext(),"Enter Valide Price",Toast.LENGTH_SHORT).show();
                   }

               }else
               {
                   Toast.makeText(getApplicationContext(),"Enter Name and Price",Toast.LENGTH_SHORT).show();
               }

            }
        });
        builder.setView(v);
        AlertDialog dialog=builder.create();
        dialog.setTitle("ADD DRINK");
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {  if(data!=null) {
            Uri uri=data.getData();
            if(uri.getPath().isEmpty()==false)
            {   CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).setAutoZoomEnabled(true).setAspectRatio(4,5).start(this);
            }

           }
        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK)
        {  CropImage.ActivityResult result = CropImage.getActivityResult(data);
           Uri resultUri = result.getUri();
            if(resultUri.getPath().isEmpty()==false)
            {  String filePath = getRealPathFromUri(resultUri);
                uploaded_file = new File(filePath);
                image.setImageURI(resultUri);
            }
        }
        }

    public String getRealPathFromUri(final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(this, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(this, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(this, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

private void load_data()
{api.GetDrinks(menu.getID()+"").enqueue(new Callback<List<drink>>() {
    @Override
    public void onResponse(Call<List<drink>> call, Response<List<drink>> response) {
        adapter=new DrinkAdapter(response.body(),Drink_layout.this,menu);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailure(Call<List<drink>> call, Throwable t) {
        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
    }
});

}


}

