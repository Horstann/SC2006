package com.example.agoraproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agoraproject.adapters.sliderItemAdapter;
import com.example.agoraproject.backend.AgoraBackEndInterface;
import com.example.agoraproject.backend.BackEndArrayResource;
import com.example.agoraproject.backend.BackEndResource;
import com.example.agoraproject.backend.RetroFitClient;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The main page pertaining to a particular product. Displays product information: an image of
 * the product, its price, a written description, and current buying thresholds. Allows the user to
 * buy or like the product.
 * @author Jun Wei Teo
 */
public class productPageActivity extends AppCompatActivity {
    TextView block1,block2,block3,block4,block5,prod_thres_progress_TV,dist_TV;
    TextView bar_units_TV,bar_units_TV2,bar_units_TV3,bar_units_TV4,bar_units_TV5,bar_units_TV6;
    static final float COORDINATE_OFFSET = 0.00002f;
    TextView prodName, prodPrice,prodDescription,qty,closingtime_TV;
    ImageView prodImage;            //Image of the item the user wants
    String pName,pDes,pPrice,pProductId;
    String pDistance = "0";       // Attributes of product the user wants
    Button add, minus, buyNow,paynow;
    ImageButton like_button,share_friend_IB,share_nearby_IB,viewMap;
    SliderView image_Slide;
    String[] images;
    int totalUnits ;     // Product Total Units
    int TotalBought;     // Product Total Bought
    float[] price_threshold;     // Product Price Threshold
    int[] unit_threshold;     // Product Unit Threshold
    int quantity = 0;               // Quantity of product the user wants
    String authToken;
    public static String strSeparator = "__,__";
    String closingtime;
    double sellerlat,sellerlong,sellerdistance;
    double userlat,userlong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        SharedPreferences sp = getSharedPreferences("UserAuthKey", MODE_PRIVATE);
        authToken = sp.getString("USER_AUTHKEY", null);
        userlat = Double.parseDouble(sp.getString("USERLAT","1.3461427"));
        userlong = Double.parseDouble(sp.getString("USERLONG","103.6791842"));
        //Toast.makeText(this,"LAT is "+userlat+ " Long is "+ userlong,Toast.LENGTH_LONG).show();

        share_friend_IB = this.findViewById(R.id.share_friend_IB);
        share_nearby_IB = this.findViewById(R.id.share_nearby_IB);

        // For Threshold Progress
        prod_thres_progress_TV = this.findViewById(R.id.prod_thres_progress_TV);
        block1 = this.findViewById(R.id.tv_bar_1);
        block2 = this.findViewById(R.id.tv_bar_2);
        block3 = this.findViewById(R.id.tv_bar_3);
        block4 = this.findViewById(R.id.tv_bar_4);
        block5 = this.findViewById(R.id.tv_bar_5);

        closingtime_TV=this.findViewById(R.id.closingtime_TV);

        bar_units_TV2 = this.findViewById(R.id.bar_units_TV2);
        bar_units_TV3= this.findViewById(R.id.bar_units_TV3);
        bar_units_TV4 = this.findViewById(R.id.bar_units_TV4);
        bar_units_TV5 = this.findViewById(R.id.bar_units_TV5);
        bar_units_TV6 = this.findViewById(R.id.bar_units_TV6);
        paynow = this.findViewById(R.id.paynow_button);


        // For Page Utilities
        dist_TV = this.findViewById(R.id.distance_TV);
        prodName=this.findViewById(R.id.productName_TV);
        prodPrice = this.findViewById(R.id.productPrice_Textview);
        prodDescription = this.findViewById(R.id.productDes_TV);
        like_button = this.findViewById(R.id.like_Button);
        prodImage = this.findViewById(R.id.product_imageView);
        add = this.findViewById(R.id.qtyadd_button);
        minus = this.findViewById(R.id.qtyminus_button);
        qty = this.findViewById(R.id.qty_textView);
        buyNow = this.findViewById(R.id.buyNow_button);
        image_Slide = this.findViewById(R.id.prodImage_slider);
        viewMap = this.findViewById(R.id.showMap_IB);
        UserProductDataHelper updb = new UserProductDataHelper(this);

        Intent intCollect = getIntent();
        Bundle b = intCollect.getExtras();
        if(b!= null){
            pProductId = b.getString("p1");         // Product Id
            pName =  b.getString("p2");             // Product Name
            pPrice =  b.getString("p3");            // Product Price
            pDes =  b.getString("p4");              // Product Description
            images  = b.getStringArray("p5");       // Product Images
            totalUnits = b.getInt("p6");     // Product Total Units
            TotalBought = b.getInt("p7");     // Product Total Bought
            price_threshold = b.getFloatArray("p8");     // Product Price Threshold
            unit_threshold = b.getIntArray("p9");     // Product Unit Threshold
            sellerdistance = b.getDouble("p10");         //Disctance
            sellerlat = b.getDouble("p11");
            sellerlong = b.getDouble("p12");
            closingtime=b.getString("p13");
            Log.e("TEST",sellerlat+" LOL "+sellerlong);
            pDistance = String.format("%.2f",sellerdistance)+"KM";         //Disctance
            //Log.e("TEST",closingtime);


            closingtime_TV.setText(" Queue Closing on "+closingtime);
            dist_TV.setText(" Distance to Seller: "+pDistance);
            block1.setText("$"+String.format("%.2f",price_threshold[0]));
            block2.setText("$"+String.format("%.2f",price_threshold[1]));
            block3.setText("$"+String.format("%.2f",price_threshold[2]));
            block4.setText("$"+String.format("%.2f",price_threshold[3]));
            block5.setText("$"+String.format("%.2f",price_threshold[4]));
            bar_units_TV2.setText(String.valueOf(unit_threshold[0]));
            bar_units_TV3.setText(String.valueOf(unit_threshold[1]));
            bar_units_TV4.setText(String.valueOf(unit_threshold[2]));
            bar_units_TV5.setText(String.valueOf(unit_threshold[3]));
            bar_units_TV6.setText(String.valueOf(unit_threshold[4]));


            int nextThreshold = unit_threshold[4];
            if(TotalBought == 0){
                block1.setBackgroundColor(getResources().getColor(R.color.yellow));
                prod_thres_progress_TV.setText("Currently "+String.valueOf(TotalBought)+" Units Bought!  "+String.valueOf(unit_threshold[0])+" More To Next Target!");
            }
            else{
                if(TotalBought > 0  ){
                    block1.setBackgroundColor(getResources().getColor(R.color.green));
                    if(TotalBought < unit_threshold[0]){
                        block1.setBackgroundColor(getResources().getColor(R.color.yellow));
                        nextThreshold = unit_threshold[0];
                    }
                }
                if(TotalBought > unit_threshold[0]){
                    block2.setBackgroundColor(getResources().getColor(R.color.green));
                    if(TotalBought < unit_threshold[1]){
                        block2.setBackgroundColor(getResources().getColor(R.color.yellow));
                        nextThreshold = unit_threshold[1];

                    }
                }
                if(TotalBought > unit_threshold[1]){
                    block3.setBackgroundColor(getResources().getColor(R.color.green));
                    if(TotalBought < unit_threshold[2]){
                        block3.setBackgroundColor(getResources().getColor(R.color.yellow));
                        nextThreshold = unit_threshold[2];

                    }
                }
                if(TotalBought > unit_threshold[2]){
                    block4.setBackgroundColor(getResources().getColor(R.color.green));
                    if(TotalBought < unit_threshold[3]){
                        block4.setBackgroundColor(getResources().getColor(R.color.yellow));
                        nextThreshold = unit_threshold[3];
                    }
                }
                if(TotalBought > unit_threshold[3]){
                    block5.setBackgroundColor(getResources().getColor(R.color.green));
                    if(TotalBought < unit_threshold[4]){
                        block5.setBackgroundColor(getResources().getColor(R.color.yellow));
                        nextThreshold = unit_threshold[4];
                    }
                }if(TotalBought > unit_threshold[4]){
                    block5.setBackgroundColor(getResources().getColor(R.color.yellow));
                    nextThreshold = TotalBought;
                }

                int more = nextThreshold - TotalBought;
                prod_thres_progress_TV.setText("Currently "+String.valueOf(TotalBought)+" Units Bought!  "+String.valueOf(more)+" More To Next Target!");
                //Log.e("TEST",pDes);
            }
            prodName.setText(pName);
            prodDescription.setText(pDes);
            prodPrice.setText(pPrice);
            isClosed();
        }
        //payment();
        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(productPageActivity.this,showsellerMapActivity.class);
                i.putExtra("p1",userlat);
                i.putExtra("p2",userlong);
                i.putExtra("p3",sellerlat);
                i.putExtra("p4",sellerlong);
                startActivity(i);
            }
        });


        sliderItemAdapter adapter = new sliderItemAdapter(images);
        image_Slide.setSliderAdapter(adapter);
        image_Slide.setIndicatorAnimation(IndicatorAnimationType.WORM);
        image_Slide.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);

        like_button.setOnClickListener(new View.OnClickListener() { // when user likes an item, it gets add into local SQLite Database
            @Override
            public void onClick(View view) {
                like_button.setEnabled(false);
                // convert string[] to string
                String images_conv = convertArrayToString(images);
                // convert int[] to string[]
                // convert string[] to string
                String[] price_thr_int = new String[5];
                String[] unit_thr_int = new String[5];
                for(int i =0; i < 5;i++){
                    price_thr_int[i] = String.valueOf(price_threshold[i]);
                    unit_thr_int[i] = String.valueOf(unit_threshold[i]);
                }
                String con_Price_Threshold = convertArrayToString(price_thr_int);
                String con_Unit_Threshold = convertArrayToString(unit_thr_int);
                updb.insertUserProductData(pProductId,pName,pPrice,pDes,images_conv,TotalBought,totalUnits,con_Price_Threshold,con_Unit_Threshold,sellerdistance,closingtime,sellerlat,sellerlong);
            }
        });
        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paynow.setVisibility(View.INVISIBLE);
                finish();
                Intent i = new Intent(productPageActivity.this,PaymentActivity.class);
                i.putExtra("productId",pProductId);
                startActivity(i);
            }
        });


        share_friend_IB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Shared With AgoraFriends!",Toast.LENGTH_LONG).show();
            }
        });
        share_nearby_IB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Shared With Nearby Agora Buyers!",Toast.LENGTH_LONG).show();

            }
        });


        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Submit
                buyItem();
            }
        });

    }
    public void qtyClick (View view){
        if(view.getId() == R.id.qtyadd_button){
            quantity +=1;
            qty.setText(String.valueOf(quantity));
        }
        else if(view.getId() == R.id.qtyminus_button) {
            if(quantity > 0) {
                quantity -= 1;
                qty.setText(String.valueOf(quantity));
            }
        }
    }
    public void openChat(View view){
        Intent i = new Intent(productPageActivity.this, Chat_Activity.class);
        startActivity(i);
    }
    public static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }
    public boolean payment (){
        HashMap<String, Object> map = new HashMap<>();
        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
        map.put("productId",pProductId);
        map.put("authKey", authToken);
        map.put("cmd", "checkClosingTime");

        Call<BackEndResource> call = api.getInfo(map);
        call.enqueue(new Callback<BackEndResource>() {
            @Override
            public void onResponse(Call<BackEndResource> call, Response<BackEndResource> response) {
                Log.e("TEST", "onResponse code: " + response.code());
                Log.e("TEST", "onResponse Get Closing Status:" + response.body().getStatus());
                if (response.body().getStatus() == 0) {
                    if(response.body().isClosed()){
                        paynow.setVisibility(View.VISIBLE);
                    }
                } else if (response.body().getStatus() == 2) {

                }
            }

            @Override
            public void onFailure(Call<BackEndResource> call, Throwable t) {
                Log.e("TEST", "onFailure: get hello :" + t.getMessage());
            }
        });
        return false;
    }
    public boolean buyItem (){
        LoadingAlert la = new LoadingAlert(productPageActivity.this);
        la.startAlertDialog();
        HashMap<String, Object> map = new HashMap<>();
        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);

        map.put("quantity",quantity);
        map.put("productId",pProductId);
        map.put("authKey", authToken);
        map.put("cmd", "buyProduct");

        Call<BackEndResource> call = api.getInfo(map);
        call.enqueue(new Callback<BackEndResource>() {
            @Override
            public void onResponse(Call<BackEndResource> call, Response<BackEndResource> response) {
                Log.e("TEST", "onResponse code: " + response.code());
                Log.e("TEST", "onResponse Get Closing Status:" + response.body().getStatus());
                if (response.body().getStatus() == 0) {
                    Toast.makeText(productPageActivity.this,"Succesfully Queued!",Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(productPageActivity.this,MainPageActivity.class));
                } else{

                }
                la.stopAlertDialog();
            }

            @Override
            public void onFailure(Call<BackEndResource> call, Throwable t) {
                Log.e("TEST", "onFailure: get hello :" + t.getMessage());
            }
        });
        return false;
    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void isClosed(){
        HashMap<String, Object> map = new HashMap<>();
        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
        Log.e("TEST Backend Current Order: ",authToken);
        map.put("productId",pProductId);
        map.put("authKey", authToken);
        map.put("cmd", "checkClosingTime");
        System.out.println(Arrays.asList(map)); // method 1
        Call<BackEndResource> call = api.getInfo(map);
        call.enqueue(new Callback<BackEndResource>() {
            @Override
            public void onResponse(Call<BackEndResource> call, Response<BackEndResource> response) {
                Log.e("TEST" , "onResponse code Login: "+response.code());
                if(response.code()==200){
                    Log.e("TEST" , "onResponse loadBuyerProducts: "+response.body().getStatus());
                    if(response.body().getStatus() == 0){
                        if(response.body().isClosed()){
                            prod_thres_progress_TV.setText("TIMES UP QUEUE HAS BEEN CLOSED!");
                            prod_thres_progress_TV.setBackgroundColor(getResources().getColor(R.color.red));
                            buyNow.setVisibility(View.INVISIBLE);
                            add.setEnabled(false);
                        }
                    }
                    else {
                        Toast.makeText(productPageActivity.this,"Error Please Try Again Later! \nStatus: "+response.body().getStatus(),Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(productPageActivity.this,"Error With Backend! "+response.code(),Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<BackEndResource> call, Throwable t) {

            }
        });
    }
}