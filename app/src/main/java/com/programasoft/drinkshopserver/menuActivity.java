package com.programasoft.drinkshopserver;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.programasoft.drinkshopserver.Model.error;
import com.programasoft.drinkshopserver.Model.menu;
import com.programasoft.drinkshopserver.Retrofit.IDrinkShopApi;
import com.programasoft.drinkshopserver.Utils.Comment;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class menuActivity extends AppCompatActivity {
    private static final int PICK_IMAGE =5000 ;
    private ImageView image_menu;
    private menu menu;
    private IDrinkShopApi mservice;
    private File uploaded_file;
    private EditText txt_name;
    private TextView lbl_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        image_menu=(ImageView)this.findViewById(R.id.image_menu);
        lbl_name=(TextView)this.findViewById(R.id.lbl_name);
        txt_name=(EditText) this.findViewById(R.id.txt_name);
        menu=(menu)this.getIntent().getSerializableExtra("menu");
        lbl_name.setText(menu.getName());
        Picasso.with(this).load(menu.getLink()).into(image_menu);
        mservice=Comment.getApi();
    }

    public void delete(View view) {
        final android.app.AlertDialog  progressDialog=new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        progressDialog.show();
        mservice.DeleteMenu(menu.getID()+"",menu.getName()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
             if(response.isSuccessful()) {
                 if (response.body()) {
                     progressDialog.dismiss();
                     Intent i = new Intent(getApplicationContext(), MainActivity.class);
                     getApplicationContext().startActivity(i);

                 } else {

                     Toast.makeText(getApplicationContext(), "Try Agin", Toast.LENGTH_SHORT).show();
                 }
             }else
             { progressDialog.dismiss();
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
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    public void change_image(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri ImageUri = data.getData();
                if (ImageUri.getPath().isEmpty() == false) {
                    CropImage.activity(ImageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(3, 4).start(this);

                }

            }

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();
            if (resultUri.getPath().isEmpty() == false) {
                String filePath = getRealPathFromUri(resultUri);
                uploaded_file = new File(filePath);
                image_menu.setImageURI(resultUri);
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

    public void update(View view) {
        if(txt_name.getText().toString().isEmpty()==false)
        {   final android.app.AlertDialog  progressDialog=new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
            progressDialog.show();
                if(uploaded_file!=null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), uploaded_file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", txt_name.getText().toString()+".jpg", requestFile);
                    mservice.UploadMenuImage(body).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"image uploaded",Toast.LENGTH_LONG).show();
                            menu.setLink(Comment.SERVER_URL+"menu_img/"+txt_name.getText().toString()+".jpg");
                            mservice.UpdateMenu(menu.getID()+"",txt_name.getText().toString(), menu.getLink()).enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    if(response.isSuccessful()) {
                                        if (response.body()) {
                                            progressDialog.dismiss();
                                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                            menuActivity.this.finish();
                                            getApplicationContext().startActivity(i);

                                        } else {

                                            Toast.makeText(getApplicationContext(), "Try Agin", Toast.LENGTH_SHORT).show();
                                        }
                                    }else
                                    { progressDialog.dismiss();
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
                                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

                }else {
                    mservice.UpdateMenu(menu.getID() + "", txt_name.getText().toString(), menu.getLink()).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                if (response.body()) {
                                    progressDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    menuActivity.this.finish();
                                    getApplicationContext().startActivity(i);

                                } else {

                                    Toast.makeText(getApplicationContext(), "Try Agin", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialog.dismiss();
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
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
        }else
        {
            Toast.makeText(this,"enter new name",Toast.LENGTH_LONG).show();
        }
    }
}

