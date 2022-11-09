package com.example.agoraproject.adapters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.agoraproject.models.AgoraProductModels;
import com.example.agoraproject.models.FaveItemModel;
import com.example.agoraproject.appinterfaces.FaveItemRVInterface;
import com.example.agoraproject.R;
import com.example.agoraproject.UserProductDataHelper;
import com.example.agoraproject.productPageActivity;

import java.util.ArrayList;

public class FavItem_Activitiy extends AppCompatActivity implements FaveItemRVInterface {
    ArrayList<AgoraProductModels> favemodels = new ArrayList<>();
    UserProductDataHelper updb;
    FavItemRCAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_item_activitiy);
        setupFaveModels();
        //UserProductDataHelper updb = new UserProductDataHelper(this);

    }
    private void setupFaveModels(){
        updb = new UserProductDataHelper(this);
        RecyclerView rcv = findViewById(R.id.fav_RecyclerView);
        favemodels = updb.getFaveData();
        adapter  = new FavItemRCAdapter(this,favemodels,this);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        adapter.SetOnItemClickListener(new FavItemRCAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                //delete
                updb.deleteFavItem(favemodels.get(pos).getProductName());
                favemodels.remove(pos);
                adapter.notifyItemRemoved(pos);
            }
        });

    }

    @Override
    public void onItemClicked(int pos) {
        Intent i  = new Intent(FavItem_Activitiy.this, productPageActivity.class);
        i.putExtra("p1",  favemodels.get(pos).getProductId());      // Product Id
        i.putExtra("p2",  favemodels.get(pos).getProductName());    // Product Name will change to time
        i.putExtra("p3",  favemodels.get(pos).getItemPrice());       // Product Price Currently
        i.putExtra("p4",  favemodels.get(pos).getProductdes());      // Product Description
        i.putExtra("p5",  favemodels.get(pos).getItemImage());       // Product Image
        i.putExtra("p6",  favemodels.get(pos).getTotal_units());      // Product Total Units
        i.putExtra("p7",  favemodels.get(pos).getTotal_bought());     // Product Total Bought
        i.putExtra("p8",  favemodels.get(pos).getPrice_threshold());  // Product Price Threshold
        i.putExtra("p9",  favemodels.get(pos).getUnit_threshold());   // Product Unit Threshold
        i.putExtra("p10", favemodels.get(pos).getDistancetoseller());   // Distance
        //i.putExtra("p11", pLat[pos]);   // lat
        //i.putExtra("p12", pLong[pos]);  // long to add into model
        i.putExtra("p13", favemodels.get(pos).getClosingtime());  // long to add into model
        startActivity(i);
    }

    @Override
    public void onDeleteClick(int pos) {
        updb.deleteFavItem(favemodels.get(pos).getProductName());
        favemodels.remove(pos);
        // notify
        adapter.notifyItemRemoved(pos);
    }
}