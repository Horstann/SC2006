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

import com.example.agoraproject.backend.AgoraBackEndInterface;
import com.example.agoraproject.backend.BackEndResource;
import com.example.agoraproject.backend.RetroFitClient;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    Button done;
    ImageView imageView;
    TextView totalprice_TV;
    String authToken;
    String imageURl;
    String productId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        SharedPreferences sp = getSharedPreferences("UserAuthKey", MODE_PRIVATE);
        authToken = sp.getString("USER_AUTHKEY", null);
        done = this.findViewById(R.id.payment_done_button);
        totalprice_TV=this.findViewById(R.id.totalprice_TV);
        imageView = this.findViewById(R.id.payment_qrcode_IV);
        Intent intCollect = getIntent();
        Bundle b = intCollect.getExtras();
        if(b!= null){
            Double total = b.getDouble("PRICE");
            imageURl = b.getString("QRURL");
            totalprice_TV.setText("PLEASE PAY $"+total+" THANK YOU!");
            Log.e("TEST",imageURl);
            Picasso.get().load(imageURl).into(imageView);
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(PaymentActivity.this,MainPageActivity.class));
            }
        });
    }

}