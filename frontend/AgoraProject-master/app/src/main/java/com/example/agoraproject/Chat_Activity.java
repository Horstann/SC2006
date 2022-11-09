package com.example.agoraproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agoraproject.backend.AgoraBackEndInterface;
import com.example.agoraproject.backend.BackEndArrayResource;
import com.example.agoraproject.backend.BackEndResource;
import com.example.agoraproject.backend.RetroFitClient;
import com.example.agoraproject.models.AgoraProductModels;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat_Activity extends AppCompatActivity {
    List<chat_message_models> c  = new ArrayList<>();
    ListView listMsg;
    EditText txtmsg;
    chat_message_adapter adapter;
    ImageButton send,chat_users_map_IB;
    Button payment;
    String pProductId;
    String pPrice;
    String pName;
    int qty;
    String imageUrl;
    ImageView chat_product_IV;
    TextView chat_item_name_TV,chat_item_price_TV;
    ArrayList<Double> fLat = new ArrayList<>();
    ArrayList<Double> fLong = new ArrayList<>();
    LoadingAlert la = new LoadingAlert(Chat_Activity.this);
    Double totalcost;
    String authToken;
    int userflag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences sp = getSharedPreferences("UserAuthKey", MODE_PRIVATE);
        authToken = sp.getString("USER_AUTHKEY", null);
        fLat.add(1.3356686);
        fLong.add(103.7079985);

        fLat.add(1.3409449);
        fLong.add(103.6902565);

        fLat.add(1.3456);
        fLong.add(103.6786);

        fLat.add(1.3550754);
        fLong.add(103.6825646);
        fLat.add(1.353224);
        fLong.add(103.6864989);

        fLat.add(1.3502623);
        fLong.add(103.6788627);
        fLat.add(1.3466691);
        fLong.add(103.6806973);

        fLat.add(1.3457);
        fLong.add(103.6802);

        fLat.add(1.3427);
        fLong.add(103.6807);

        fLat.add(1.32576657609);
        fLong.add(103.724887553);

        fLat.add(1.32576657609);
        fLong.add(103.724887553);

        fLat.add(1.3331001);
        fLong.add(103.6799868);

        payment = this.findViewById(R.id.payment_button);
        chat_users_map_IB= this.findViewById(R.id.chat_users_map_IB);
        chat_item_price_TV = this.findViewById(R.id.chat_item_price_TV);
        chat_item_name_TV = this.findViewById(R.id.chat_item_name_TV);
        chat_product_IV = this.findViewById(R.id.chat_product_IV);
        listMsg= this.findViewById(R.id.chat_listview);
        txtmsg = this.findViewById(R.id.user_chat_enter_ET);
        send = this.findViewById(R.id.send_txt_IB);
        payment.setVisibility(View.INVISIBLE);

        Intent intCollect = getIntent();
        Bundle b = intCollect.getExtras();
        if(b!= null) {
            pProductId = b.getString("p1");         // Product Id
            pName = b.getString("p2");             // Product Name
            pPrice = b.getString("p3");            // Product Price
            imageUrl =b.getString("p4");
            qty = b.getInt("p5");
            userflag = b.getInt("p6");
            Picasso.get().load(imageUrl).into(chat_product_IV);
            chat_item_price_TV.setText(pPrice);
            chat_item_name_TV.setText(pName);
            String cleanPrice = pPrice.replace("$","");
            totalcost = Double.parseDouble(cleanPrice)*qty;
            checkClosingTime();
            Log.e("TEST",qty+"");
        }
        chat_users_map_IB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Chat_Activity.this,showProductQueuemap.class);
                i.putExtra("Lat",fLat);
                i.putExtra("Long",fLong);
                startActivity(i);
            }
        });
        setMsg();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userflag == 0){
                    c.add(new chat_message_models("Me",txtmsg.getText().toString(),false));
                }
                else if (userflag==1){
                    c.add(new chat_message_models("Me (Seller) ",txtmsg.getText().toString(),false));
                }
                adapter.notifyDataSetChanged();
                listMsg.setSelection(adapter.getCount()-1);
                txtmsg.setText("");
            }
        });
        adapter = new chat_message_adapter(this,c);
        listMsg.setAdapter(adapter);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go new Activity
                la.startAlertDialog();
                loadQRCode();
            }
        });

    }
    public void setMsg(){
        c.add(new chat_message_models("Buyer "+String.valueOf(1),"Hello! I'm asking more people to buy!",true));
        c.add(new chat_message_models("Buyer "+String.valueOf(2),"Hello! Everyone!",true));
        c.add(new chat_message_models("Buyer "+String.valueOf(3),"I'm so Excited to get this!!",true));

        if(userflag == 0){
            c.add(new chat_message_models("Seller ","Thanks! Please message me if you have any queries!",true));
            c.add(new chat_message_models("Me "," Hello!",false));

        }
        else if (userflag==1){
            c.add(new chat_message_models("Me (Seller) ","Thanks! Please message me if you have any queries!",false));
        }

    }
    public void checkClosingTime() {
        HashMap<String, Object> map = new HashMap<>();
        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
        Log.e("TEST Backend: ",authToken);
        map.put("productId", pProductId);//lzLRcwwSKTZfd7hu9PGN
        //map.put("productId", "lzLRcwwSKTZfd7hu9PGN");
        map.put("authKey", authToken);
        map.put("cmd", "checkClosingTime");
        Call<BackEndResource> call = api.getInfo(map);
        call.enqueue(new Callback<BackEndResource>() {
            @Override
            public void onResponse(Call<BackEndResource> call, Response<BackEndResource> response) {
                if(response.body().getStatus() == 0){
                    if(response.body().isClosed()){
                        Log.e("TEST" , "onResponse code Login: "+response.code());
                        Log.e("TEST" , "onResponse checKIsClodes: "+response.body().getStatus());
                        payment.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<BackEndResource> call, Throwable t) {
                la.stopAlertDialog();
            }
        });
    }
    public void loadQRCode(){
        HashMap<String, Object> map = new HashMap<>();
        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
        map.put("productId","lzLRcwwSKTZfd7hu9PGN");
        map.put("authKey", authToken);
        map.put("cmd", "loadQRCode");

        Call<BackEndResource> call = api.getInfo(map);
        call.enqueue(new Callback<BackEndResource>() {
            @Override
            public void onResponse(Call<BackEndResource> call, Response<BackEndResource> response) {
                Log.e("TEST", "onResponse code: " + response.code());
                if(response.code()==200){
                    Log.e("TEST", "onResponse Get LoadQR Status:" + response.body().getStatus());
                    if (response.body().getStatus() == 0) {
                        String URL = response.body().getQrcode();
                        Intent i = new Intent(Chat_Activity.this,PaymentActivity.class);
                        i.putExtra("QRURL",URL);
                        i.putExtra("PRICE",totalcost);
                        startActivity(i);
                    }else {
                        Toast.makeText(Chat_Activity.this,"Error Please Try Again Later! \nStatus: "+response.body().getStatus(),Toast.LENGTH_LONG).show();
                        la.stopAlertDialog();
                    }
                }
                else{
                    Toast.makeText(Chat_Activity.this,"Error With Backend "+response.code(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BackEndResource> call, Throwable t) {
                Log.e("TEST", "onFailure: get hello :" + t.getMessage());
                la.stopAlertDialog();
            }
        });
    }
}