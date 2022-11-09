package com.example.agoraproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agoraproject.models.AgoraProductModels;
import com.example.agoraproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridMainAdapter extends BaseAdapter {
    Context context;
    ArrayList<AgoraProductModels> gridModels;
    LayoutInflater inflater;

    public GridMainAdapter(Context context,ArrayList<AgoraProductModels> gridModels){ // may add RCView InterFace
        this.context = context;
        this.gridModels = gridModels;
    }

    @Override
    public int getCount() {
        return gridModels.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(inflater == null){
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(view == null){
            view = inflater.inflate(R.layout.grid_items,null);
        }

        ImageView prodImage = view.findViewById(R.id.grid_image);
        TextView gpName = view.findViewById(R.id.grid_product_name);
        TextView gpPrice =  view.findViewById(R.id.grid_product_price);
        if(gridModels.get(i).getItemImage().length>0){
            Picasso.get().load(gridModels.get(i).getItemImage()[0]).into(prodImage);
        }
        else{
            Picasso.get().load("https://static8.depositphotos.com/1003153/893/v/450/depositphotos_8938809-stock-illustration-example-rubber-stamp.jpg").into(prodImage);
        }

        //prodImage.setImageResource(gridModels.get(i).getItemImage()[0]);
        gpName.setText(gridModels.get(i).getProductName());
        gpPrice.setText(gridModels.get(i).getItemPrice());
        return view;
    }
}
