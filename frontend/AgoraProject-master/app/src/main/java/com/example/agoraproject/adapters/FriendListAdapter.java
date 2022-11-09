package com.example.agoraproject.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.agoraproject.R;
import com.example.agoraproject.chat_message_models;
import com.example.agoraproject.models.userFriendModels;

import java.util.ArrayList;
import java.util.List;

public class FriendListAdapter extends BaseAdapter {
    Context context;
    ArrayList<userFriendModels> models;

    public FriendListAdapter(Context context, ArrayList<userFriendModels> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        userFriendModels friend = models.get(i);
        view = mInflater.inflate(R.layout.friends_row,
                null);
        TextView username = (TextView)view.findViewById(R.id.friend_list_TV);
        TextView txtMsg = (TextView) view.findViewById(R.id.friend_list_email_TV);

        username.setText(friend.getUsername());
        txtMsg.setText(friend.getUseremail());

        return view;
    }
}
