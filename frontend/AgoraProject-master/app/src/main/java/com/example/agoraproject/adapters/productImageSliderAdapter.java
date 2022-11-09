package com.example.agoraproject.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.agoraproject.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class productImageSliderAdapter extends SliderViewAdapter<productImageSliderAdapter.Holder> {
    ArrayList<Uri> uriArrayList;
    ImageView imageView;
    public productImageSliderAdapter(Context context, ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    @Override
    public productImageSliderAdapter.Holder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_slide_item,parent,false);
        return new productImageSliderAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(productImageSliderAdapter.Holder viewHolder, int pos) {
        Picasso.get().load(uriArrayList.get(pos)).into(imageView);
    }

    @Override
    public int getCount() {
        return uriArrayList.size();
    }

    public class Holder extends SliderViewAdapter.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slide_imageVIew);
        }
    }
}
