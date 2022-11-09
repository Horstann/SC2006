package com.example.agoraproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agoraproject.R;
import com.example.agoraproject.appinterfaces.RCViewInterface;
import com.example.agoraproject.models.AgoraSocialsModels;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Social_RV_Adapter extends RecyclerView.Adapter<Social_RV_Adapter.MyViewHolder> {
    Context context;
    ArrayList<AgoraSocialsModels> models;
    private final RCViewInterface rcViewInterface;

    public Social_RV_Adapter(Context context, ArrayList<AgoraSocialsModels> models,RCViewInterface rcViewInterface) {
        this.context = context;
        this.models=models;
        this.rcViewInterface=rcViewInterface;
    }

    @NonNull
    @Override
    public Social_RV_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.social_rows,parent,false);
        return new Social_RV_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Social_RV_Adapter.MyViewHolder holder, int position) {
        holder.name.setText(models.get(position).getProductName());
        holder.price.setText(models.get(position).getItemPrice());
        holder.req.setText(models.get(position).getUserName() + " has sent a Buy Request to you!");
        Picasso.get().load(models.get(position).getItemImage()[0]).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name,price,req;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.share_imageView);
            name = itemView.findViewById(R.id.share_prod_TV);
            price = itemView.findViewById(R.id.share_price_TV);
            req = itemView.findViewById(R.id.social_friendreq_TV);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(rcViewInterface!=null){
                       if(getAdapterPosition() != RecyclerView.NO_POSITION){
                           rcViewInterface.onItemClick(getAdapterPosition());
                       }
                    }
                }
            });
        }
    }
}
