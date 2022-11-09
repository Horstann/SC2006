package com.example.agoraproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.agoraproject.adapters.FriendListAdapter;
import com.example.agoraproject.models.userFriendModels;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class FriendList_Activity extends AppCompatActivity implements AddUserDialog.AddUserListener {
    FloatingActionButton fab;
    ListView listFriends;
    FriendListAdapter adapter;
    LoadingAlert loadingAlert = new LoadingAlert(FriendList_Activity.this);
    ArrayList<userFriendModels> models = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        loadUsers();
        listFriends = this.findViewById(R.id.friends_listView);
        fab = this.findViewById(R.id.add_friends_FAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUserDialog addUserDialog = new AddUserDialog();
                addUserDialog.show(getSupportFragmentManager(),"example");
            }
        });
        adapter = new FriendListAdapter(this,models);
        listFriends.setAdapter(adapter);
    }

    @Override
    public void applyText(String username, String email) {
        // adding here
        models.add(new userFriendModels(username,email));
        adapter.notifyDataSetChanged();


    }
    public void loadUsers(){
        models.add(new userFriendModels("Alex Lim","alexlim02@kmail.com"));
        models.add(new userFriendModels("Toh Xiao Ming","txmlovescar@kmail.com"));
        models.add(new userFriendModels("Jerry Tom","jtom12312@kmail.com"));
    }
}