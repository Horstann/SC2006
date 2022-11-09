package com.example.agoraproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agoraproject.R;
import com.example.agoraproject.appinterfaces.ShoppingCartInterface;
import com.example.agoraproject.models.AgoraProductModels;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class ShoppingCart_RCVAdapter extends RecyclerView.Adapter<ShoppingCart_RCVAdapter.MyViewHolder> {
    ArrayList<AgoraProductModels> scm;
    Context c;
    private final ShoppingCartInterface sci;
    public ShoppingCart_RCVAdapter(Context c , ArrayList<AgoraProductModels> scm,ShoppingCartInterface sci) {
        this.scm = scm;
        this.c = c;
        this.sci = sci;
    }

    @NonNull
    @Override
    public ShoppingCart_RCVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View v = inflater.inflate(R.layout.pending_shoppingcart_row,parent,false);
        return new ShoppingCart_RCVAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCart_RCVAdapter.MyViewHolder holder, int pos) {
        holder.pName.setText(scm.get(pos).getProductName());
        holder.pPrice.setText(scm.get(pos).getItemPrice());
        Picasso.get().load(scm.get(pos).getItemImage()[0]).into(holder.pImage);
    }

    @Override
    public int getItemCount() {
        return scm.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView pName,pPrice,pStatus;
        ImageButton chat_manage_button,del_manage_button;
        ImageView pImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pName = itemView.findViewById(R.id.manage_product_name);
            pPrice = itemView.findViewById(R.id.manage_price_textView);
            pStatus = itemView.findViewById(R.id.manage_product_status);
            pImage = itemView.findViewById(R.id.manage_prod_imageview);
            chat_manage_button = itemView.findViewById(R.id.chat_manage_button);
            del_manage_button = itemView.findViewById(R.id.del_manage_button);
            del_manage_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(sci!= null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            sci.onDeleteClick(pos);
                        }
                    }
                }
            });
            chat_manage_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(sci!= null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            sci.onChatClick(pos);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(sci!= null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            sci.onItemClicked(pos);
                        }
                    }
                }
            });
        }
    }
}
