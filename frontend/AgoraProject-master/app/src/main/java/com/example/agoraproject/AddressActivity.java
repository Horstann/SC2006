package com.example.agoraproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.agoraproject.backend.AgoraBackEndInterface;
import com.example.agoraproject.backend.BackEndResource;
import com.example.agoraproject.backend.RetroFitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends AppCompatActivity {
    EditText address, postalcode;
    RequestQueue mQueue;
    String AuthToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        address = this.findViewById(R.id.Address_ET);
        postalcode = this.findViewById(R.id.PostalCode_ET);
        SharedPreferences prefs = getSharedPreferences("UserAuthKey", Context.MODE_PRIVATE);
        AuthToken = prefs.getString("USER_AUTHKEY","");
        mQueue = Volley.newRequestQueue(this);
        Log.e("TEST TOKEN", AuthToken);
    }

    public String[] getLatLong(String PostalCode){
        String url = "https://developers.onemap.sg/commonapi/search?searchVal="+PostalCode+"&returnGeom=Y&getAddrDetails=Y&pageNum=1";
        String[] longlat = new String[2];
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int checkfound = response.getInt("found");
                    int check = 0;//checkfound.getInt("found");
                    Log.e("TEST",""+checkfound);
                    if(checkfound>0){
                        JSONArray jsonArray = response.getJSONArray("results");
                        JSONObject product = jsonArray.getJSONObject(0);
                        Log.e("TEST",product+"");
                        Log.e("TEST",product.getString("LONGITUDE"));
                        Log.e("TEST",product.getString("LATITUDE"));
                        longlat[0] = product.getString("LONGITUDE");
                        longlat[1] =product.getString("LATITUDE");
                        float lon = Float.parseFloat(longlat[0]);
                        float lat = Float.parseFloat(longlat[1]);

                        SharedPreferences pref = getSharedPreferences("UserAuthKey", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("USERLAT",String.valueOf(lat));
                        editor.putString("USERLONG",String.valueOf(lat));
                        editor.commit();

                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        Log.e("TEST","ADDRESS DONE");

                        Log.e("TEST",AuthToken);
                        Log.e("TEST",String.valueOf(lon));
                        Log.e("TEST",String.valueOf(lat));


                        HashMap<String, Object> map1 =  new HashMap<>();
                        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
                        map1.put("qrcode","https://airnfts.s3.amazonaws.com/nft-images/20210909/Random_QR_code_3_1631181316556.jpeg");
                        map1.put("homeAddr",address.getText().toString());
                        map1.put("homeLong",lon);
                        map1.put("homeLat",lat);
                        map1.put("newAuthKey",AuthToken);
                        map1.put("authKey","");
                        map1.put("cmd","signUp");

                        Call<BackEndResource> call = api.getInfo(map1);
                        call.enqueue(new Callback<BackEndResource>() {
                            @Override
                            public void onResponse(Call<BackEndResource> call, Response<BackEndResource> response) {
                                Log.e("TEST" , "onResponse code: "+response.code());
                                Log.e("TEST" , "onResponse: get Sign In Status :"+response.body().getStatus());
                                if(response.code()==200){
                                    if(response.body().getStatus() == 0){

                                        SharedPreferences pref = getSharedPreferences("UserAuthKey", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("USER_ADDRESS",address.getText().toString());
                                        editor.commit();

                                        finish();
                                        Intent i = new Intent(AddressActivity.this,MainPageActivity.class);
                                        startActivity(i);
                                    }
                                    else{
                                        Toast.makeText(AddressActivity.this,"Error Please Try Again Later! \nStatus: "+response.body().getStatus(),Toast.LENGTH_LONG).show();
                                    }
                                }
                                else {
                                    Toast.makeText(AddressActivity.this,"Error With Backend " + response.code(),Toast.LENGTH_LONG).show();
                                }

                            }
                            @Override
                            public void onFailure(Call<BackEndResource> call, Throwable t) {
                                Log.e("TEST" , "onFailure: get hello :"+ t.getMessage());
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
        return longlat;
    }


    public void addressClick (View view){
        String postal = postalcode.getText().toString();
        String Address = address.getText().toString();
        new AlertDialog.Builder(AddressActivity.this)
                .setTitle("Distributor Option")
                .setMessage("Would you like to be a Distributor? \nAdditional Discount may be given!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getLatLong(postal);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getLatLong(postal);
                    }
                })
                .show();


//        HashMap<String, Object> map1 =  new HashMap<>();
//        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
//        map1.put("homeAddr","NTU SCSE");
//        map1.put("homeLong",0.0);
//        map1.put("homeLat",0.0);
//        map1.put("newAuthKey",AuthToken);
//        map1.put("authKey","");
//        map1.put("cmd","signUp");
//
//        Call<BackEndResource> call = api.getInfo(map1);
//        call.enqueue(new Callback<BackEndResource>() {
//            @Override
//            public void onResponse(Call<BackEndResource> call, Response<BackEndResource> response) {
//                Log.e("TEST" , "onResponse code: "+response.code());
//                Log.e("TEST" , "onResponse: get Sign In Status :"+response.body().getStatus());
//                if(response.body().getStatus() == 0){
//                    Intent i = new Intent(AddressActivity.this,MainPageActivity.class);
//                    startActivity(i);
//                }else{
//
//                }
//            }
//            @Override
//            public void onFailure(Call<BackEndResource> call, Throwable t) {
//                Log.e("TEST" , "onFailure: get hello :"+ t.getMessage());
//            }
//        });
//        if(view.getId() == R.id.finish_button){
//            if(isEmpty(address)){
//                Toast.makeText(getApplicationContext(), "Please Enter Your Address!", Toast.LENGTH_SHORT).show();
//
//            }else if(isEmpty(postalcode)){
//                Toast.makeText(getApplicationContext(), "Please Enter Your Postal Code!", Toast.LENGTH_SHORT).show();
//            }else{
                //Intent i = new Intent(AddressActivity.this,MainPageActivity.class);
                //i.putExtra("Address",address.getText());
                //i.putExtra("PostalCode",postalcode.getText().toString());
                //startActivity(i);

            }
        //}
    //}

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
}