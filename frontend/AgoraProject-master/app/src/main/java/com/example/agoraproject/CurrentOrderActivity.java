package com.example.agoraproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.agoraproject.adapters.ShoppingCart_RCVAdapter;
import com.example.agoraproject.appinterfaces.ShoppingCartInterface;
import com.example.agoraproject.backend.AgoraBackEndInterface;
import com.example.agoraproject.backend.BackEndArrayResource;
import com.example.agoraproject.backend.BackEndResource;
import com.example.agoraproject.backend.RetroFitClient;
import com.example.agoraproject.models.AgoraProductModels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class CurrentOrderActivity extends AppCompatActivity implements ShoppingCartInterface {
    TextView accName_TV,changePwd_TV,orderTV;
    String username;
    String authToken;
    ShoppingCart_RCVAdapter scAdapter;
    RequestQueue mQueue;
    ArrayList<AgoraProductModels> scm = new ArrayList<>();
    ArrayList<Integer> TotalQtyBuyerBought = new ArrayList<Integer>();
    RecyclerView rcv;
    ArrayList<Integer> unitsBought = new ArrayList<>();
    int op = R.drawable.iphone;
    double[] pLat={1.3327,1.3277,1.3331,1.3333,1.3348};
    double[] pLong={103.6789,103.6783,103.7436,103.7402,103.7468};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);
        mQueue = Volley.newRequestQueue(this);

        SharedPreferences sp = getSharedPreferences("UserAuthKey", MODE_PRIVATE);
        authToken = sp.getString("USER_AUTHKEY", null);

        accName_TV = this.findViewById(R.id.userNameAc_TV);
        rcv = this.findViewById(R.id.pendingItemRecyclerView);
        orderTV = this.findViewById(R.id.order_TV);
        //jsonParse();
        if(scm.size() == 0){
            orderTV.setText("Currently No Active Orders");
        }
        else{
            orderTV.setText("Currently Active Orders");
        }
        scAdapter = new ShoppingCart_RCVAdapter(this,scm,this);
        rcv.setAdapter(scAdapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        Intent intCollect = getIntent();
        Bundle b = intCollect.getExtras();
        if(b!= null){
            username =  b.getString("p1");
            accName_TV.setText("Welcome "+username+"!");
        }
        getProductsFromBackend();
    }
    private void jsonParse() {
        int total_units = 100;
        int[] unit_threshold = {10,15,20,25,30};
        String url = "https://dummyjson.com/products";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("products");
                    for(int i = 1; i < 2;i++){
                        int total_bought = 21;
                        Log.e("TEST", "INSIDE JSON VOLLEY COA");
                        JSONObject product = jsonArray.getJSONObject(i);
                        String title = product.getString("title");
                        String des = product.getString("description");
                        int price = product.getInt("price");
                        float curPrice = 0;
                        unitsBought.add(5);
                        float[] price_threshold = new float [5];
                        JSONArray aa = product.getJSONArray("images");
                        String[] images = new String[aa.length()];
                        for(int k = 0; k < aa.length();k++)
                        {
                            images[k] = aa.get(k).toString();
                        }
                        total_bought =  (int)(Math.random()*(unit_threshold[4]-2+1)+2);
                        curPrice = (float) price;
                        for(int p = 0; p < 5; p++){
                            price_threshold[p] = price;
                            if(total_bought>unit_threshold[p]){
                                curPrice = price;
                            }
                            price = price - (int)(price*0.078);
                        }
                        scm.add(new AgoraProductModels("0",title,des,"$"+String.valueOf(curPrice),images,total_units,total_bought,price_threshold,unit_threshold,15.76,"21/11/2022 11:00",0.0,0.0));
                        Log.e("TEST", "Agora onResponse: " + title);
                        scAdapter.notifyDataSetChanged();
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

    @Override
    public void onItemClicked(int pos) {
        Intent i  = new Intent(CurrentOrderActivity.this,productPageActivity.class);
        i.putExtra("p1",  scm.get(pos).getProductId());      // Product Id
        i.putExtra("p2",  scm.get(pos).getProductName());    // Product Name will change to time
        i.putExtra("p3",  scm.get(pos).getItemPrice());       // Product Price Currently
        i.putExtra("p4",  scm.get(pos).getProductdes());      // Product Description
        i.putExtra("p5",  scm.get(pos).getItemImage());       // Product Image
        i.putExtra("p6",  scm.get(pos).getTotal_units());      // Product Total Units
        i.putExtra("p7",  scm.get(pos).getTotal_bought());     // Product Total Bought
        i.putExtra("p8",  scm.get(pos).getPrice_threshold());  // Product Price Threshold
        i.putExtra("p9",  scm.get(pos).getUnit_threshold());   // Product Unit Threshold
        i.putExtra("p10", scm.get(pos).getDistancetoseller());   // Distance
        i.putExtra("p11", scm.get(pos).getSellerlat());   // lat
        i.putExtra("p12", scm.get(pos).getSellerlong());  // long to add into model
        i.putExtra("p13",scm.get(pos).getClosingtime());
        startActivity(i);
    }

    @Override
    public void onDeleteClick(int pos) {
        HashMap<String, Object> map = new HashMap<>();
        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
        String ProduName = scm.get(pos).getProductName();
        map.put("quantity",2);
        map.put("productId",scm.get(pos).getProductId());
        map.put("dropAll",true);
        map.put("authKey", authToken);
        map.put("cmd", "dropProduct");

        Call<BackEndResource> call = api.getInfo(map);
        call.enqueue(new Callback<BackEndResource>() {
            @Override
            public void onResponse(Call<BackEndResource> call, retrofit2.Response<BackEndResource> response) {
                Log.e("TEST", "onResponse code: " + response.code());
                if(response.code()==200){
                    Log.e("TEST", "onResponse: get QRCode Status :" + response.body().getStatus());
                    if (response.body().getStatus() == 0) {
                        scm.remove(pos);
                        scAdapter.notifyDataSetChanged();
                        Toast.makeText(CurrentOrderActivity.this, ProduName + " Has Been Succesfully Dropped!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(CurrentOrderActivity.this, "Error Please Try Again Later! \nStatus: " + response.body().getStatus(), Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(CurrentOrderActivity.this,"Error With Backend " + response.code(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BackEndResource> call, Throwable t) {

            }
        });
    }

    @Override
    public void onChatClick(int pos) {
        Intent i = new Intent(CurrentOrderActivity.this,Chat_Activity.class);
        i.putExtra("p1",scm.get(pos).getProductId()); //pid
        i.putExtra("p2",scm.get(pos).getProductName());
        i.putExtra("p3",scm.get(pos).getItemPrice());
        i.putExtra("p4",scm.get(pos).getItemImage()[0]);
        i.putExtra("p5",unitsBought.get(pos));
//       i.putExtra("p5",TotalQtyBuyerBought.get(pos));

        startActivity(i);
    }
    public void getProductsFromBackend() {
        HashMap<String, Object> map = new HashMap<>();
        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
        Log.e("TEST Backend Current Order: ",authToken);

        map.put("authKey", authToken);
        map.put("cmd", "loadBuyerProducts");
        //System.out.println(Arrays.asList(map)); // method 1
        Call<BackEndArrayResource> call = api.getProductArray(map);
        call.enqueue(new Callback<BackEndArrayResource>() {
            @Override
            public void onResponse(@NonNull Call<BackEndArrayResource> call, @NonNull retrofit2.Response<BackEndArrayResource> response) {
                BackEndArrayResource resource = response.body();

                Log.e("TEST" , "onResponse code Login: "+response.code());
                if(response.code()== 200){
                    Log.e("TEST" , "onResponse loadBuyerProducts: "+response.body().getStatus());
                    if(response.body().getStatus() == 0){
                        ArrayList<BackEndResource> backEndResources = new ArrayList<>(Arrays.asList(resource.getProductArray()));
                        if(backEndResources.size()>0){
                            orderTV.setText("Currently Active Orders");
                        }
                        else{
                            orderTV.setText("Currently No Active Orders");
                        }
                        for(int i = 0; i < backEndResources.size();i++){
                            String prod_id= backEndResources.get(i).getProductId();
                            String prod_name = backEndResources.get(i).getName();
                            String prod_des = backEndResources.get(i).getDesc();
                            String[] prod_pics_base64 = backEndResources.get(i).getPics();
                            //TIME
                            double distance = backEndResources.get(i).getDistanceFromUser();
                            String closingtime = backEndResources.get(i).getClosingTime();
                            int total_units =  backEndResources.get(i).getTotalUnits();
                            int total_bought =  backEndResources.get(i).getTotalBought();
                            float[] price_threshold =  backEndResources.get(i).getPriceThresholds();
                            int[] unit_threshold =  backEndResources.get(i).getUnitThresholds();
                            unitsBought.add(backEndResources.get(i).getUnitsBought());
                            float curPrice = price_threshold[0];
                            for(int p = 0; p < 4; p++){
                                if(total_bought>unit_threshold[p]){
                                    curPrice = price_threshold[p+1];
                                }
                            }
                            Log.e("TEST From Backend", prod_name);
                            TotalQtyBuyerBought.add(backEndResources.get(i).getTotalBought());
                            scm.add((new AgoraProductModels(prod_id,prod_name,prod_des,"$"+String.format("%.2f",curPrice),prod_pics_base64,total_units,total_bought,price_threshold,unit_threshold,distance,closingtime,0.0,0.0)));
                            scAdapter.notifyDataSetChanged();
                            orderTV.setText("Currently Active Orders");
                        }
                    }
                    else{
                        Toast.makeText(CurrentOrderActivity.this,"Error Please Try Again Later! \nStatus: "+response.body().getStatus(),Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(CurrentOrderActivity.this,"Error With Backend! "+response.code(),Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<BackEndArrayResource> call, Throwable t) {
                Log.e("TEST", "onFailure: get hello :" + t.getMessage());
            }
        });

    }
}