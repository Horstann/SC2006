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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agoraproject.adapters.productImageSliderAdapter;
import com.example.agoraproject.backend.AgoraBackEndInterface;
import com.example.agoraproject.backend.BackEndArrayResource;
import com.example.agoraproject.backend.BackEndResource;
import com.example.agoraproject.backend.RetroFitClient;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles the uploading of a product by the seller. The seller must provide the item's image,
 * name, price threshold, unit threshold, total units, end time, and description.
 * @author Jun Wei Teo
 */
public class Upload_Product_Activity extends AppCompatActivity {
    ArrayList<String> imagesb64 = new ArrayList<>();
    String authToken;
    Button uploadImage,list_item_button;
    EditText upload_prod_name_ET,upload_prod_price_Threshold_ET,upload_prod_Threshold_ET,upload_prod_totalunits_ET,upload_prod_endtime_ET,upload_DES_ET;
    ArrayList<Uri> urilist = new ArrayList<>();
    SliderView image_Slide;
    productImageSliderAdapter adapter;
    LoadingAlert la =new LoadingAlert(Upload_Product_Activity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        SharedPreferences sp = getSharedPreferences("UserAuthKey", MODE_PRIVATE);
        authToken = sp.getString("USER_AUTHKEY", null);


        upload_prod_name_ET = this.findViewById(R.id.upload_prod_name_ET);
        upload_prod_price_Threshold_ET= this.findViewById(R.id.upload_prod_price_Threshold_ET);
        upload_prod_Threshold_ET= this.findViewById(R.id.upload_prod_Threshold_ET);
        upload_prod_totalunits_ET = this.findViewById(R.id.upload_prod_totalunits_ET);
        upload_prod_endtime_ET = this.findViewById(R.id.upload_prod_endtime_ET);
        upload_DES_ET= this.findViewById(R.id.upload_DES_ET);

        list_item_button = this.findViewById(R.id.list_item_button);
        image_Slide = this.findViewById(R.id.uploadImage_slider);
        uploadImage = this.findViewById(R.id.image_uploadButton);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(Upload_Product_Activity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Upload_Product_Activity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                }
                else{
                    selectImage();
                }
            }
        });

        list_item_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmpty(upload_prod_name_ET)){
                    Toast.makeText(getApplicationContext(),"Please Enter Product Name",Toast.LENGTH_LONG).show();
                }
                else if(isEmpty(upload_prod_price_Threshold_ET)){
                    Toast.makeText(getApplicationContext(),"Please Enter Product Price Threshold",Toast.LENGTH_LONG).show();
                }
                else if(isEmpty(upload_prod_Threshold_ET)){
                    Toast.makeText(getApplicationContext(),"Please Enter Product Threshold",Toast.LENGTH_LONG).show();
                }
                else if(isEmpty(upload_prod_totalunits_ET)){
                    Toast.makeText(getApplicationContext(),"Please Enter Product Units",Toast.LENGTH_LONG).show();
                }
                else if(isEmpty(upload_prod_endtime_ET)){
                    Toast.makeText(getApplicationContext(),"Please Enter Product End Time",Toast.LENGTH_LONG).show();
                }
                else if(isEmpty(upload_DES_ET)){
                    Toast.makeText(getApplicationContext(),"Please Enter Product Description",Toast.LENGTH_LONG).show();
                }

                String closingtime = upload_prod_endtime_ET.getText().toString();
                String[] pic_base64 = imagesb64.toArray(new String[imagesb64.size()]);
                String name = upload_prod_name_ET.getText().toString();
                int totalunits= Integer.parseInt(upload_prod_totalunits_ET.getText().toString());
                int[] price_threshold = Ints.toArray(
                        Lists.transform(
                                Splitter.on(',')
                                        .omitEmptyStrings()
                                        .splitToList(upload_prod_price_Threshold_ET.getText().toString()),
                                Ints.stringConverter()));

                int[] unit_threshold = Ints.toArray(
                        Lists.transform(
                                Splitter.on(',')
                                        .omitEmptyStrings()
                                        .splitToList(upload_prod_Threshold_ET.getText().toString()),
                                Ints.stringConverter()));;
                //closing time
                String des = upload_DES_ET.getText().toString();
                Log.d("TEST",name);
                Log.d("TEST",String.valueOf(totalunits));
                Log.d("TEST",String.valueOf(price_threshold.length));
                Log.d("TEST",String.valueOf(unit_threshold.length));
                Log.d("TEST",closingtime);
                Log.d("TEST",des);
                la.startAlertDialog();

                HashMap<String, Object> map = new HashMap<>();
                AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);

                map.put("pics",pic_base64);
                map.put("desc",des);
                map.put("closingTime",closingtime);
                map.put("unitThresholds",unit_threshold);
                map.put("priceThresholds",price_threshold);
                map.put("totalUnits",totalunits);
                map.put("name",name);
                map.put("authKey", authToken);
                map.put("cmd", "addProduct");

                Call<BackEndResource> call = api.getInfo(map);
                call.enqueue(new Callback<BackEndResource>() {
                    @Override
                    public void onResponse(Call<BackEndResource> call, Response<BackEndResource> response) {
                        Log.e("TEST", "onResponse code: " + response.code());
                        if(response.code()==200){
                            Log.e("TEST", "onResponse: addProduct Status :" + response.body().getStatus());
                            if (response.body().getStatus() == 0) {
                                la.stopAlertDialog();
                                finish();
                                startActivity(new Intent(Upload_Product_Activity.this,MainPageActivity.class));
                            }
                            else{
                                Toast.makeText(Upload_Product_Activity.this,"Error Please Try Again Later! \nStatus: "+response.body().getStatus(),Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(Upload_Product_Activity.this,"Error With Backend! "+response.code(),Toast.LENGTH_LONG).show();
                        }


                    }
                    @Override
                    public void onFailure(Call<BackEndResource> call, Throwable t) {
                        Log.e("TEST", "onFailure: get hello :" + t.getMessage());
                    }
                });
            }
        });

        adapter = new productImageSliderAdapter(this,urilist);
        image_Slide.setSliderAdapter(adapter);
        image_Slide.setIndicatorAnimation(IndicatorAnimationType.WORM);
        image_Slide.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        image_Slide.startAutoCycle();
        image_Slide.stopAutoCycle();
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("image/*");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image")
        ,100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            // Granted Permission
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
            if (data.getClipData() != null) {
                int x = data.getClipData().getItemCount();
                for (int i = 0; i < x; i++) {
                    urilist.add(data.getClipData().getItemAt(i).getUri());
                    Uri image = data.getClipData().getItemAt(i).getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),image);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        byte[] byteArray = outputStream.toByteArray();
                        String encodedString = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        imagesb64.add(encodedString);
                        Log.e("TEST ENCODED64 1 ",encodedString);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
    public void tessto() {
        HashMap<String, Object> map = new HashMap<>();
        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);

        map.put("pics","");
        map.put("desc","");
        map.put("closingTime","");
        map.put("unitThresholds","");
        map.put("priceThresholds","");
        map.put("totalUnits","");
        map.put("name","");
        map.put("authKey", authToken);
        map.put("cmd", "addProduct");

        Call<BackEndResource> call = api.getInfo(map);
        call.enqueue(new Callback<BackEndResource>() {
            @Override
            public void onResponse(Call<BackEndResource> call, Response<BackEndResource> response) {
                Log.e("TEST", "onResponse code: " + response.code());
                Log.e("TEST", "onResponse: get Sign In Status :" + response.body().getStatus());
                if (response.body().getStatus() == 0) {

                } else if (response.body().getStatus() == 2) {

                }
            }

            @Override
            public void onFailure(Call<BackEndResource> call, Throwable t) {
                Log.e("TEST", "onFailure: get hello :" + t.getMessage());
            }
        });
    }
    public void rip() {
        HashMap<String, Object> map = new HashMap<>();
        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);

        map.put("pageLength","");
        map.put("beginIndex","");
        map.put("searchTerm","");
        map.put("authKey", authToken);
        map.put("cmd", "searchProducts");

        Call<BackEndArrayResource> call = api.getProductArray(map);
        call.enqueue(new Callback<BackEndArrayResource>() {
            @Override
            public void onResponse(Call<BackEndArrayResource> call, Response<BackEndArrayResource> response) {
               BackEndArrayResource resource = response.body();
               ArrayList<BackEndResource> backEndResources = new ArrayList<>(Arrays.asList(resource.getProductArray()));
               String prod_name = backEndResources.get(0).getName();
            }

            @Override
            public void onFailure(Call<BackEndArrayResource> call, Throwable t) {
                Log.e("TEST", "onFailure: get hello :" + t.getMessage());
            }
        });
    }
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;
        return true;
    }
}


//                HashMap<String, Object> map = new HashMap<>();
//                AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
//                map.put("pic",pic_base64[1]);
//                map.put("authKey", "");
//                map.put("cmd", "testImageUpload");
////
//                Call<BackEndResource> call = api.getInfo(map);
//                call.enqueue(new Callback<BackEndResource>() {
//                    @Override
//                    public void onResponse(Call<BackEndResource> call, Response<BackEndResource> response) {
//                        Log.e("TEST", "onResponse code: " + response.code());
//                        Log.e("TEST", "onResponse: get Sign In Status :" + response.body().getStatus());
//                        if (response.body().getStatus() == 0) {
//                                Log.e("TEST",response.body().getUrl());
//                        } else if (response.body().getStatus() == 2) {
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<BackEndResource> call, Throwable t) {
//                        Log.e("TEST", "onFailure: get hello :" + t.getMessage());
//                    }
//                });