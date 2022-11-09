package com.example.agoraproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agoraproject.backend.AgoraBackEndInterface;
import com.example.agoraproject.backend.BackEndResource;
import com.example.agoraproject.backend.RetroFitClient;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerQrCodeActivity extends AppCompatActivity {
    String authToken;
    String pProductId;
    TextView PricePayment_TV;
    Button payment_qrdone_button;
    int qty;
    double price;
    ImageView qr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_qr_code);
        SharedPreferences sp = getSharedPreferences("UserAuthKey", MODE_PRIVATE);
        authToken = sp.getString("USER_AUTHKEY", null);
        qr = this.findViewById(R.id.qr_IV);
        PricePayment_TV= this.findViewById(R.id.PricePayment_TV);
        payment_qrdone_button = this.findViewById(R.id.payment_qrdone_button);
        Intent intCollect = getIntent();
        Bundle b = intCollect.getExtras();
        if(b!= null) {
            pProductId = b.getString("ProductId");
            qty=b.getInt("ProductQuantity");
            price= Double.parseDouble(b.getString("ProductPrice").replace("$",""));
            Double d = qty*price;
            Toast.makeText(this,"PLEASE PAY "+ String.format("%.2f",d),Toast.LENGTH_LONG).show();
            //PricePayment_TV.setText("PLEASE MAKE A PAYMENT OF "+String.format("%.2f",qty*price)+ " THANK YOU!");
            //getQRCode();
        }
        payment_qrdone_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
    public void getQRCode() {
        HashMap<String, Object> map = new HashMap<>();
        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
        Log.e("TEST Backend: ",authToken);

        map.put("loadQRCode", pProductId);
        map.put("authKey", authToken);
        map.put("cmd", "loadQRCode");

        Call<BackEndResource> call = api.getInfo(map);
        call.enqueue(new Callback<BackEndResource>() {
            @Override
            public void onResponse(Call<BackEndResource> call, Response<BackEndResource> response) {
                if(response.body().getStatus() == 0){
                    String qrUrl = response.body().getQrcode();
                    Picasso.get().load(qrUrl).into(qr);
                }
            }

            @Override
            public void onFailure(Call<BackEndResource> call, Throwable t) {

            }
        });

    }
}