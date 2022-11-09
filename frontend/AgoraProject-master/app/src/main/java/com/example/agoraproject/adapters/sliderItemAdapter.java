package com.example.agoraproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.agoraproject.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

public class sliderItemAdapter extends SliderViewAdapter<sliderItemAdapter.Holder> {
    String[] images;
    ImageView imageView;
    // change to string array url
    public  sliderItemAdapter(String[] images){
        this.images = images;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_slide_item,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int pos) {
        //viewHolder.imageView.setImageResource(images[pos]);
        // picasso here add here
        Picasso.get().load(images[pos]).into(imageView);

    }

    @Override
    public int getCount() {
        return images.length;
    }

    public class Holder extends  SliderViewAdapter.ViewHolder{

        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slide_imageVIew);
        }
    }
}
