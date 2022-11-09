package com.example.agoraproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agoraproject.backend.AgoraBackEndInterface;
import com.example.agoraproject.backend.BackEndResource;
import com.example.agoraproject.backend.RetroFitClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {
    TextView name,email,id,address;
    Button upload_QR_code_Button;
    String authToken;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        name= this.findViewById(R.id.username_TV) ;
        email=this.findViewById(R.id.email_TV) ;;
        address = this.findViewById(R.id.address_TV);
        upload_QR_code_Button=this.findViewById(R.id.upload_QR_code_Button);

        SharedPreferences sp = getSharedPreferences("UserAuthKey", MODE_PRIVATE);
        authToken = sp.getString("USER_AUTHKEY", null);
        String add = sp.getString("USER_ADDRESS", "TESTING AVE BLOCK 1");

        // address save in SharedPrefs
        //id=this.findViewById(R.id.username_TV) ;;
        image = this.findViewById(R.id.imageView10);
        Intent intCollect = getIntent();
        Bundle b = intCollect.getExtras();
        if(b!= null) {
            String name1 = b.getString("NAME_USER");         // Product Id
            String email1 = b.getString("NAME_EMAIL");             // Product Name
            String id1 = b.getString("NAME_personID");            // Product Price
            Uri image1 = Uri.parse(b.getString("NAME_URL"));
            Picasso.get().load(image1).into(image);
            name.setText(name1);
            email.setText(email1);
            //address.setText(add);
            getAddress();
        }
        upload_QR_code_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(AccountActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AccountActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                }
                else{
                    selectImage();
                }
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("image/*");
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
//        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image")
                ,100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            // Granted Permission
            Log.e("TEST","Permission Granted");
            selectImage();
        }else{
            Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK && data!=null) {
            // Granted Permission
            Log.e("TEST ENCODED64 1 ","ACTIVITY RESULTS OUTSIDE");

            if (data.getData() != null) {
                //int x = data.getClipData().getItemCount();
                //for (int i = 0; i < x; i++) {
                Log.e("TEST ENCODED64 1 ","ACTIVITY RESULTS");
                Uri image = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),image);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        byte[] byteArray = outputStream.toByteArray();
                        String encodedString = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        Log.e("TEST ENCODED64 1 ",encodedString);

                        HashMap<String, Object> map = new HashMap<>();
                        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);

                        map.put("qrcode",encodedString);
                        map.put("homeLong",null);
                        map.put("homeLat",null);
                        map.put("deleteAccount",false);
                        map.put("authKey", authToken);
                        map.put("cmd", "editAccount");

                        Call<BackEndResource> call = api.getInfo(map);
                        call.enqueue(new Callback<BackEndResource>() {
                            @Override
                            public void onResponse(Call<BackEndResource> call, Response<BackEndResource> response) {
                                Log.e("TEST", "onResponse code: " + response.code());
                                if(response.code()==200){
                                    Log.e("TEST", "onResponse: get QRCode Status :" + response.body().getStatus());
                                    if (response.body().getStatus() == 0) {
                                        //Log.e("TEST QRURL",response.body().getUrl());
                                    }
                                    else {
                                        Toast.makeText(AccountActivity.this, "Error Please Try Again Later! \nStatus: " + response.body().getStatus(), Toast.LENGTH_LONG).show();
                                    }
                                }
                                else{
                                    Toast.makeText(AccountActivity.this,"Error With Backend " + response.code(),Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BackEndResource> call, Throwable t) {
                                Log.e("TEST", "onFailure: get hello :" + t.getMessage());
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                //}
            }
        }
    }
    public void getAddress(){
        HashMap<String, Object> map = new HashMap<>();
        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
        map.put("authKey", authToken);
        map.put("cmd", "viewAccount");
        Call<BackEndResource> call = api.getInfo(map);
        call.enqueue(new Callback<BackEndResource>() {
            @Override
            public void onResponse(Call<BackEndResource> call, Response<BackEndResource> response) {
                Log.e("TEST", "onResponse code: " + response.code());
                if(response.code()==200){
                    Log.e("TEST", "onResponse: get Address :" + response.body().getStatus());
                    if (response.body().getStatus() == 0) {
                        //Log.e("TEST QRURL",response.body().getUrl());
                        address.setText(response.body().getHomeAddr());
                    }
                    else {
                        Toast.makeText(AccountActivity.this, "Error Please Try Again Later! \nStatus: " + response.body().getStatus(), Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(AccountActivity.this,"Error With Backend " + response.code(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BackEndResource> call, Throwable t) {

            }
        });
    }
}