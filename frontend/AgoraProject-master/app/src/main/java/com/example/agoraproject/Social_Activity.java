package com.example.agoraproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.agoraproject.adapters.Social_RV_Adapter;
import com.example.agoraproject.appinterfaces.RCViewInterface;
import com.example.agoraproject.models.AgoraSocialsModels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Social_Activity extends AppCompatActivity implements RCViewInterface {
    RequestQueue mQueue;
    TextView social_friend_tv;
    Social_RV_Adapter adapter;
    String[] users = {"Alex Lim","John Tan","Lin Jun Jie","Lim Kai Ming","Loo Shu Hu"};
    ArrayList<AgoraSocialsModels> agoraProductModels = new ArrayList<>();
    ArrayList<Double> sellerLat = new ArrayList<>();
    ArrayList<Double> sellerLong = new ArrayList<>();
    ArrayList<String> closing = new ArrayList<>();

    String authToken;
    Double userlat;
    Double userlong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        mQueue = Volley.newRequestQueue(this);
        social_friend_tv = this.findViewById(R.id.social_friend_tv);
        SharedPreferences sp = getSharedPreferences("UserAuthKey", MODE_PRIVATE);
        authToken = sp.getString("USER_AUTHKEY", null);
        userlat = Double.parseDouble(sp.getString("USERLAT","1.3461427"));
        userlong = Double.parseDouble(sp.getString("USERLONG","103.6791842"));

        RecyclerView recyclerView = this.findViewById(R.id.req_RV);
        jsonParse();
        adapter = new Social_RV_Adapter(this,agoraProductModels,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        social_friend_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Social_Activity.this,FriendList_Activity.class));
            }
        });
    }

    //
    private void jsonParse() {
        int total_units = 100;
        int total_bought = 21;
        sellerLat.add(1.4300707);
        sellerLong.add(103.7605883);
        sellerLat.add(1.359181);
        sellerLong.add(103.7595373);
        sellerLat.add(1.3610673);
        sellerLong.add(103.736453);
        closing.add( "12/12/2022 12:12");
        closing.add( "11/11/2022 11:11");
        closing.add( "15/11/2022 09:30");


        int[] unit_threshold = {10,15,20,25,30};
        String url = "https://dummyjson.com/products";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("products");
                    int useridnum =0 ;
                    for(int i = 7; i < 10;i++){
                        Log.e("TEST", "INSIDE JSON VOLLEY");
                        JSONObject product = jsonArray.getJSONObject(i);
                        String title = product.getString("title");
                        String des = product.getString("description");
                        int price = product.getInt("price");
                        int curPrice = 0;
                        float [] price_threshold = new float[5];
                        JSONArray aa = product.getJSONArray("images");
                        String[] images = new String[aa.length()];
                        for(int k = 0; k < aa.length();k++)
                        {
                            images[k] = aa.get(k).toString();
                        }
                        for(int p = 0; p < 5; p++){
                            price_threshold[p] = price;
                            if(total_bought>unit_threshold[p]){
                                curPrice = price;
                            }
                            price = price - (int)(price*0.078);
                        }
                        //g_search.add((new grid_item_models("0",title,des,"$1000",images,total_units,total_bought,price_threshold,unit_threshold)));
                        agoraProductModels.add(new AgoraSocialsModels("0",title,des,"$" + String.valueOf(curPrice),images,total_units,total_bought,price_threshold,unit_threshold,users[useridnum]));
                        useridnum = useridnum + 1 ;
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
            //
    @Override
    public void onItemClick(int pos) {
        Intent i  = new Intent(Social_Activity.this,productPageActivity.class);
        i.putExtra("p1",  agoraProductModels.get(pos).getProductId());      // Product Id
        i.putExtra("p2",  agoraProductModels.get(pos).getProductName());    // Product Name will change to time
        i.putExtra("p3",  agoraProductModels.get(pos).getItemPrice());       // Product Price Currently
        i.putExtra("p4",  agoraProductModels.get(pos).getProductdes());      // Product Description
        i.putExtra("p5",  agoraProductModels.get(pos).getItemImage());       // Product Image
        i.putExtra("p6",  agoraProductModels.get(pos).getTotal_units());      // Product Total Units
        i.putExtra("p7",  agoraProductModels.get(pos).getTotal_bought());     // Product Total Bought
        i.putExtra("p8",  agoraProductModels.get(pos).getPrice_threshold());  // Product Price Threshold
        i.putExtra("p9",  agoraProductModels.get(pos).getUnit_threshold());   // Product Unit Threshold
        i.putExtra("p10", distance(userlat,userlong,sellerLat.get(pos),sellerLong.get(pos)));   // Distance
        i.putExtra("p11", sellerLat.get(pos));   // lat
        i.putExtra("p12", sellerLong.get(pos));  // long to add into model
        i.putExtra("p13",closing.get(pos));
        startActivity(i);
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
}