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

import com.example.agoraproject.models.AgoraProductModels;
import com.example.agoraproject.appinterfaces.ManagePageRVInterface;
import com.example.agoraproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManageProduct_Adapter extends RecyclerView.Adapter<ManageProduct_Adapter.MyViewHolder> {
    Context context;
    //ArrayList<ManageProductModel> manageProductModels;
    ArrayList<AgoraProductModels> manageProductModels;

    ImageView imageView;
    ImageButton del,chat,edit;
    private final ManagePageRVInterface rvInterface;
    public ManageProduct_Adapter(Context context, ArrayList<AgoraProductModels> manageProductModels,ManagePageRVInterface rvInterface) {
        this.context = context;
        this.manageProductModels = manageProductModels;
        this.rvInterface = rvInterface;
    }

    @NonNull
    @Override
    public ManageProduct_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.manage_product_row,parent,false);
        return new ManageProduct_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageProduct_Adapter.MyViewHolder holder, int position) {
        holder.pName.setText(manageProductModels.get(position).getProductName());
        holder.pPrice.setText(manageProductModels.get(position).getItemPrice());
        Picasso.get().load(manageProductModels.get(position).getItemImage()[0]).into(imageView);
    }

    @Override
    public int getItemCount() {
        return manageProductModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pName,pPrice,pStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.manage_prod_imageview);
            del = itemView.findViewById(R.id.del_manage_button);
            chat = itemView.findViewById(R.id.chat_manage_button);
            edit = itemView.findViewById(R.id.edit_manage_button);
            pPrice = itemView.findViewById(R.id.manage_price_textView);
            pName = itemView.findViewById(R.id.manage_product_name);
            pStatus = itemView.findViewById(R.id.manage_product_status);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(rvInterface!= null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            rvInterface.onItemClicked(pos);
                        }
                    }
                }
            });
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(rvInterface!= null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            rvInterface.onDeleteClick(pos);
                        }
                    }
                }
            });
            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(rvInterface!= null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            rvInterface.onChatClick(pos);
                        }
                    }
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(rvInterface!= null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            rvInterface.onEditClick(pos);
                        }
                    }
                }
            });
        }
    }
}
