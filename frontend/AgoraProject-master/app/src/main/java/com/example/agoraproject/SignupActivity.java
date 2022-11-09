package com.example.agoraproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    RadioButton buyer_RB;
    RadioButton seller_RB;
    RadioGroup selection_RG;
    Button nxt_Button;
    private View.OnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_new);
        nxt_Button= this.findViewById(R.id.submit_Button);
        buyer_RB = this.findViewById(R.id.buyer_radioButton);
        seller_RB = this.findViewById(R.id.seller_radioButton);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Testing","Testing");
                if(buyer_RB.isChecked()) {
                    Toast.makeText(getApplicationContext(),buyer_RB.getText().toString(),Toast.LENGTH_LONG).show();
                }
                else if(seller_RB.isChecked()){
                    Toast.makeText(getApplicationContext(),seller_RB.getText().toString(),Toast.LENGTH_LONG).show();
                }
                Intent i = new Intent(SignupActivity.this,MainPageActivity.class);
                startActivity(i);
            }
        };
        nxt_Button.setOnClickListener(listener);


    }
}