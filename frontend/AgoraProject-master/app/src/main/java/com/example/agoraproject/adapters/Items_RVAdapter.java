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
import com.example.agoraproject.models.item_action_model;

import java.util.ArrayList;

public class Items_RVAdapter extends RecyclerView.Adapter<Items_RVAdapter.MyViewHolder> {
    private final RCViewInterface rcv;
    Context context;
    ArrayList<item_action_model> itemModels;


    public Items_RVAdapter(Context context, ArrayList<item_action_model> itemModels, RCViewInterface recyclerViewInterface){
        this.context = context;
        this.itemModels = itemModels;
        this.rcv = recyclerViewInterface;
    }
    @NonNull
    @Override
    public Items_RVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_rv_row,parent,false);
        return new Items_RVAdapter.MyViewHolder(view,rcv);
    }

    @Override
    public void onBindViewHolder(@NonNull Items_RVAdapter.MyViewHolder holder, int position) {
        holder.pName.setText(itemModels.get(position).getItemName());
        holder.image.setImageResource(itemModels.get(position).getItemImage());
        holder.pPrice.setText(itemModels.get(position).getItemPrice());
    }

    @Override
    public int getItemCount() {
        // How many item we have
        return itemModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // sorta like on create method
        ImageView image;
        TextView pName;
        TextView pPrice;
        public MyViewHolder(@NonNull View itemView, RCViewInterface rcViewInterface) {
            super(itemView);
            image = itemView.findViewById(R.id.item_ImageView);
            pName = itemView.findViewById(R.id.desciption_TV);
            pPrice = itemView.findViewById(R.id.price_TV);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(rcViewInterface!=null){
                        int pos = getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            rcViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
