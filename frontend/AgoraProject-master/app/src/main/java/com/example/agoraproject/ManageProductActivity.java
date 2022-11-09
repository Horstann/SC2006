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

import com.example.agoraproject.adapters.ManageProduct_Adapter;
import com.example.agoraproject.appinterfaces.ManagePageRVInterface;
import com.example.agoraproject.backend.AgoraBackEndInterface;
import com.example.agoraproject.backend.BackEndArrayResource;
import com.example.agoraproject.backend.BackEndResource;
import com.example.agoraproject.backend.RetroFitClient;
import com.example.agoraproject.models.AgoraProductModels;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Allows sellers to view or delete their currently listed products, or upload new products.
 * For more details on uploading a product, view Upload_Product_Activity.
 * @author Jun Wei Teo
 */
public class ManageProductActivity extends AppCompatActivity implements ManagePageRVInterface {
    RecyclerView rcv;
    FloatingActionButton fab;
    String authToken;
    ManageProduct_Adapter adapter;
    LoadingAlert la = new LoadingAlert(ManageProductActivity.this);
    TextView manage_numprod_tv;
    ArrayList<AgoraProductModels> manageProductModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

        SharedPreferences sp = getSharedPreferences("UserAuthKey", MODE_PRIVATE);
        authToken = sp.getString("USER_AUTHKEY", null);
        Log.e("TEST SP",authToken);

        manage_numprod_tv= this.findViewById(R.id.manage_numprod_tv);

        fab = this.findViewById(R.id.add_prod_fab);
        rcv = this.findViewById(R.id.manage_item_RV);
        adapter = new ManageProduct_Adapter(this,manageProductModels,this);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));


        getProductsFromBackend();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ManageProductActivity.this,Upload_Product_Activity.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onItemClicked(int pos) {        // for RCV

        Intent i  = new Intent(ManageProductActivity.this,productPageActivity.class);
        i.putExtra("p1",  manageProductModels.get(pos).getProductId());      // Product Id
        i.putExtra("p2",  manageProductModels.get(pos).getProductName());    // Product Name will change to time
        i.putExtra("p3",  manageProductModels.get(pos).getItemPrice());       // Product Price Currently
        i.putExtra("p4",  manageProductModels.get(pos).getProductdes());      // Product Description
        i.putExtra("p5",  manageProductModels.get(pos).getItemImage());       // Product Image
        i.putExtra("p6",  manageProductModels.get(pos).getTotal_units());      // Product Total Units
        i.putExtra("p7",  manageProductModels.get(pos).getTotal_bought());     // Product Total Bought
        i.putExtra("p8",  manageProductModels.get(pos).getPrice_threshold());  // Product Price Threshold
        i.putExtra("p9",  manageProductModels.get(pos).getUnit_threshold());   // Product Unit Threshold
        i.putExtra("p10", manageProductModels.get(pos).getDistancetoseller());   // Distance
        i.putExtra("p11", manageProductModels.get(pos).getSellerlat());   // lat
        i.putExtra("p12", manageProductModels.get(pos).getSellerlong());  // long to add into model                       // Product Threshold
        startActivity(i);
    }

    @Override
    public void onDeleteClick(int pos) {
        Log.e("TEST","DELETE FROM POS "+String.valueOf(pos));
    }

    @Override
    public void onChatClick(int pos) {
        Log.e("TEST","Chat FROM POS "+String.valueOf(pos));
        Intent i = new Intent(ManageProductActivity.this,Chat_Activity.class);
        i.putExtra("p1",manageProductModels.get(pos).getProductId()); //pid
        i.putExtra("p2",manageProductModels.get(pos).getProductName());
        i.putExtra("p3",manageProductModels.get(pos).getItemPrice());
        i.putExtra("p4",manageProductModels.get(pos).getItemImage()[0]);
        i.putExtra("p5",0);
        i.putExtra("p6",1);
//       i.putExtra("p5",TotalQtyBuyerBought.get(pos));

        startActivity(i);
    }

    @Override
    public void onEditClick(int pos) {
        Log.e("TEST","Edit FROM POS "+String.valueOf(pos));

    }
    public void getProductsFromBackend() {
        la.startAlertDialog();
        HashMap<String, Object> map = new HashMap<>();
            AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
            Log.e("TEST Backend Current Selling: ",authToken);

            map.put("authKey", authToken);
            map.put("cmd", "loadSellerProducts");

            Call<BackEndArrayResource> call = api.getProductArray(map);
            call.enqueue(new Callback<BackEndArrayResource>() {
                @Override
                public void onResponse(@NonNull Call<BackEndArrayResource> call, @NonNull retrofit2.Response<BackEndArrayResource> response) {
                    BackEndArrayResource resource = response.body();

                    Log.e("TEST" , "onResponse code: "+response.code());
                    if(response.code() == 200){
                        Log.e("TEST" , "onResponse: loadSellerProducts: "+response.body().getStatus());
                        if(response.body().getStatus() == 0){
                            ArrayList<BackEndResource> backEndResources = new ArrayList<>(Arrays.asList(resource.getProductArray()));
                            manage_numprod_tv.setText("Here are the items you have Listed!");
                            for(int i = 0; i < backEndResources.size();i++){
                                String prod_id= backEndResources.get(i).getProductId();
                                String prod_name = backEndResources.get(i).getName();
                                String prod_des = backEndResources.get(i).getDesc();
                                String[] prod_pics_base64 = backEndResources.get(i).getPics();
                                int total_units =  backEndResources.get(i).getTotalUnits();
                                int total_bought =  backEndResources.get(i).getTotalBought();
                                float[] price_threshold =  backEndResources.get(i).getPriceThresholds();
                                int[] unit_threshold =  backEndResources.get(i).getUnitThresholds();
                                double distance = backEndResources.get(i).getDistanceFromUser();
                                String closingtime = backEndResources.get(i).getClosingTime();
                                Double sellerlong = backEndResources.get(i).getSellerLong();
                                Double sellerlat = backEndResources.get(i).getSellerLat();

                                float curPrice = price_threshold[0];
                                for(int p = 0; p < 4; p++){
                                    if(total_bought>unit_threshold[p]){
                                        curPrice = price_threshold[p+1];
                                    }
                                }
                                manageProductModels.add((new AgoraProductModels(prod_id,prod_name,prod_des,"$"+String.format("%.2f",curPrice),prod_pics_base64,total_units,total_bought,price_threshold,unit_threshold,distance,closingtime,sellerlat,sellerlong)));
                                adapter.notifyDataSetChanged();
                                la.stopAlertDialog();
                            }
                        }
                        else if(response.body().getStatus() == 7){
                            Toast.makeText(ManageProductActivity.this,"You Have Not Listed Anything!",Toast.LENGTH_LONG).show();
                            la.stopAlertDialog();
                        }
                        else{
                            Toast.makeText(ManageProductActivity.this,"Error Please Try Again Later! \nStatus: "+response.body().getStatus(),Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(ManageProductActivity.this,"Error With Backend! "+response.code(),Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<BackEndArrayResource> call, Throwable t) {
                    Log.e("TEST", "onFailure: get hello :" + t.getMessage());
                }
            });
    }
}