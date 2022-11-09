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
import com.example.agoraproject.models.FaveItemModel;
import com.example.agoraproject.appinterfaces.FaveItemRVInterface;
import com.example.agoraproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavItemRCAdapter extends RecyclerView.Adapter<FavItemRCAdapter.MyViewHolder> {
    ImageView imageView;
    Context context;
    ArrayList<AgoraProductModels> fModel;
    FaveItemRVInterface faveItemRVInterface;
    private OnItemClickListener listener;

    public FavItemRCAdapter(Context context, ArrayList<AgoraProductModels> fModel, FaveItemRVInterface faveItemRVInterface){
        this.context = context;
        this.fModel = fModel;
        this.faveItemRVInterface = faveItemRVInterface;
    }
    @NonNull
    @Override
    public FavItemRCAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.faverecyclerview_row,parent,false);
        return new FavItemRCAdapter.MyViewHolder(view,listener);
    }
    public interface OnItemClickListener{
        void onItemClick(int pos);
    }

    public void SetOnItemClickListener(OnItemClickListener clickListener){
        listener= clickListener;
    }
    @Override
    public void onBindViewHolder(@NonNull FavItemRCAdapter.MyViewHolder holder, int position) {
        holder.pName.setText(fModel.get(position).getProductName());
        holder.pPrice.setText(fModel.get(position).getItemPrice());
        Picasso.get().load(fModel.get(position).getItemImage()[0]).into(imageView);
    }

    @Override
    public int getItemCount() {
        return fModel.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageButton delFav_Button;
        TextView pName,pPrice,pStatus;
        public MyViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            delFav_Button = itemView.findViewById(R.id.delfav_button);
            imageView = itemView.findViewById(R.id.share_imageView);
            pName = itemView.findViewById(R.id.share_prod_TV);
            pPrice= itemView.findViewById(R.id.share_price_TV);
            pStatus = itemView.findViewById(R.id.fproduct_status);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(faveItemRVInterface!= null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            faveItemRVInterface.onItemClicked(pos);
                        }
                    }
                }
            });
            delFav_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(faveItemRVInterface!= null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            faveItemRVInterface.onDeleteClick(pos);
                        }
                    }
                }
            });
        }
    }


}
