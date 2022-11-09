package com.example.agoraproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class chat_message_adapter extends BaseAdapter {
    Context context;
    List<chat_message_models> chatmsgmodels;

    public chat_message_adapter(Context context, List<chat_message_models> chatmsgmodels) {
        this.context = context;
        this.chatmsgmodels = chatmsgmodels;
    }

    @Override
    public int getCount() {
        return chatmsgmodels.size();
    }

    @Override
    public chat_message_models getItem(int i) {
        return chatmsgmodels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        chat_message_models c = chatmsgmodels.get(i);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(c.me_or_not){
            view = mInflater.inflate(R.layout.chat_to_rows,
                    null);
        }
        else{
            view = mInflater.inflate(R.layout.chat_from_rows,
                    null);
        }
        TextView username = (TextView)view.findViewById(R.id.chat_username_tv);
        TextView txtMsg = (TextView) view.findViewById(R.id.chat_myown_TV);

        username.setText(c.getUsername());
        txtMsg.setText(c.getText());
        return view;
    }
}
