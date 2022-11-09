package com.example.agoraproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agoraproject.models.AgoraProductModels;
import com.example.agoraproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteSearchAdapter extends ArrayAdapter<AgoraProductModels> {

    private ArrayList<AgoraProductModels> itemModels;
    public AutoCompleteSearchAdapter(@NonNull Context context, @NonNull List<AgoraProductModels> objects) {
        super(context, 0,objects);
        itemModels = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return productFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.search_autocomplete_row,parent,false
            );
        }
        TextView name = convertView.findViewById(R.id.search_result_name);
        TextView price = convertView.findViewById(R.id.search_result_name2);
        ImageView icon = convertView.findViewById(R.id.search_resImage);

        AgoraProductModels g = getItem(position);

        if(g!=null){
            name.setText(g.getProductName());
            price.setText(g.getItemPrice());
            Picasso.get().load(g.getItemImage()[0]).into(icon);
        }
        return convertView;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            List<AgoraProductModels> suggestions = new ArrayList<>();

            if(charSequence==null || charSequence.length()==0){
                suggestions.addAll(itemModels);
            }
            else {
                String filterpattern = charSequence.toString().toLowerCase().trim();

                for(AgoraProductModels g : itemModels){
                    //Log.e("TEST",filterpattern);
                    //Log.e("TEST",g.getProductName().toLowerCase());
                    if(g.getProductName().toLowerCase().trim().contains(filterpattern)){
                        suggestions.add(g);
                        //Log.e("TEST","ENTERED");
                    }
                }
            }
            results.values = suggestions;
            results.count=suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            addAll((List) filterResults.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((AgoraProductModels) resultValue).getProductName();
        }
    };
}
