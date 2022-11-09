package com.example.agoraproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingAlert {
    Activity activity;
    AlertDialog dialog ;
    public LoadingAlert(Activity a){
        activity = a;
    }
    void startAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog,null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }
    void stopAlertDialog(){
        dialog.dismiss();
    }
}
