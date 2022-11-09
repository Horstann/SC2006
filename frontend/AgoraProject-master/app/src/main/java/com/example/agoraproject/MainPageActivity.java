package com.example.agoraproject;
import java.lang.Math;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.agoraproject.adapters.AutoCompleteSearchAdapter;
import com.example.agoraproject.adapters.FavItem_Activitiy;
import com.example.agoraproject.adapters.GridMainAdapter;
import com.example.agoraproject.backend.AgoraBackEndInterface;
import com.example.agoraproject.backend.BackEndArrayResource;
import com.example.agoraproject.backend.BackEndResource;
import com.example.agoraproject.backend.RetroFitClient;
import com.example.agoraproject.models.AgoraProductModels;
import com.example.agoraproject.models.item_action_model;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * The main page of the application. Allows user to browse products and displays recommended items
 * to them. A majority of the application's other functionalities can be accessed by the user
 * from here, such as ManageProductActivity and CurrentOrderActivity.
 * @author Jun Wei Teo
 */
public class MainPageActivity extends AppCompatActivity{
//public class MainPageActivity extends AppCompatActivity implements RCViewInterface{

    String usersName;
    AutoCompleteSearchAdapter adapterauto;
    Boolean jsonFlag = false;
    Boolean backendFlag = false;
    GoogleSignInOptions gsio;
    GoogleSignInClient gsc;

    RequestQueue mQueue;
    ImageButton manage_prod_IB,current_order_IB,like_prod_IB,Account_IB;
    private Object item_recyclerView;
    TextView account_TextView,logout_TextView,prod_RV_title,socials;
    EditText searchBar;
    GridView gridView;
    ImageButton searchButton;
    int op = R.drawable.iphone;

    ArrayList<AgoraProductModels> g_search = new ArrayList<>();
    //ArrayList<item_action_model> itemModels = new ArrayList<>();
    ArrayList<AgoraProductModels> product_models = new ArrayList<>();
    String displayName;
    String email;
    Uri picurl;
    String personID,authKey;
    LoadingAlert la = new LoadingAlert(MainPageActivity.this);
    AutoCompleteTextView search_bar;
    GridMainAdapter gridMainAdapter;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        socials= this.findViewById(R.id.main_social_TV);
        manage_prod_IB = this.findViewById(R.id.manage_prod_IB);
        current_order_IB = this.findViewById(R.id.current_order_IB);
        like_prod_IB = this.findViewById(R.id.like_prod_IB);
        Account_IB = this.findViewById(R.id.Account_IB);
        searchBar=this.findViewById(R.id.searchbar_editText);
        prod_RV_title = this.findViewById(R.id.prod_RV_title);
        mQueue = Volley.newRequestQueue(this);
//        SharedPreferences pref = getSharedPreferences("UserAuthKey", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("USERLAT","1.37843100800213");
//        editor.putString("USERLONG","103.762842789877");
//        editor.commit();


        //la.startAlertDialog();
        //la.stopAlertDialog();

        gsio = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gsio);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            displayName = acct.getDisplayName();
            email = acct.getEmail();
            picurl = acct.getPhotoUrl();
            personID  = acct.getId();           // get userID From Google Sign In
            authKey = acct.getIdToken();
            SharedPreferences pref1 = getSharedPreferences("UserAuthKey", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = pref1.edit();
            editor1.putString("USER_AUTHKEY",authKey);
            editor1.commit();
            Log.e("TEST ACCT!=NULL",authKey);
            loadUserLatLongSP();
        }
        //setGridItemModels();

        //jsonParse();
        getProductsFromBackend();


        gridView = findViewById(R.id.item_GridView);
        gridMainAdapter = new GridMainAdapter(this, product_models);
        gridView.setAdapter(gridMainAdapter);
        //
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent i  = new Intent(MainPageActivity.this,productPageActivity.class);
                i.putExtra("p1",  product_models.get(pos).getProductId());      // Product Id
                i.putExtra("p2",  product_models.get(pos).getProductName());    // Product Name will change to time
                i.putExtra("p3",  product_models.get(pos).getItemPrice());       // Product Price Currently
                i.putExtra("p4",  product_models.get(pos).getProductdes());      // Product Description
                i.putExtra("p5",  product_models.get(pos).getItemImage());       // Product Image
                i.putExtra("p6",  product_models.get(pos).getTotal_units());      // Product Total Units
                i.putExtra("p7",  product_models.get(pos).getTotal_bought());     // Product Total Bought
                i.putExtra("p8",  product_models.get(pos).getPrice_threshold());  // Product Price Threshold
                i.putExtra("p9",  product_models.get(pos).getUnit_threshold());   // Product Unit Threshold
                i.putExtra("p10", product_models.get(pos).getDistancetoseller());   // Distance
                i.putExtra("p11", product_models.get(pos).getSellerlat());   // lat
                i.putExtra("p12", product_models.get(pos).getSellerlong());  // long to add into model
                Log.e("TEST",product_models.get(pos).getSellerlat()+" LOLMP "+product_models.get(pos).getSellerlong());

                i.putExtra("p13", product_models.get(pos).getClosingtime());  // long to add into model
                startActivity(i);
            }
        });

        socials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainPageActivity.this,Social_Activity.class));
            }
        });
    }
    private void jsonParse() {
        int total_units = 100;
        int[] unit_threshold = {10,15,20,25,30};
        String url = "https://dummyjson.com/products";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("products");
                    for(int i = 2; i < 7;i++){
                        int total_bought = 21;
                        Log.e("TEST", "INSIDE JSON VOLLEY");
                        JSONObject product = jsonArray.getJSONObject(i);
                        String title = product.getString("title");
                        String des = product.getString("description");
                        int price = product.getInt("price");
                        float curPrice = 0;
                        float[] price_threshold = new float [5];
                        JSONArray aa = product.getJSONArray("images");
                        String[] images = new String[aa.length()];
                        for(int k = 0; k < aa.length();k++)
                        {
                            images[k] = aa.get(k).toString();
                        }
                        total_bought =  (int)(Math.random()*(unit_threshold[4]-2+1)+2);
                        curPrice = (float) price;
                        for(int p = 0; p < 5; p++){
                            price_threshold[p] = price;
                            if(total_bought>unit_threshold[p]){
                                curPrice = price;
                            }
                            price = price - (int)(price*0.078);
                        }
                        g_search.add((new AgoraProductModels("0",title,des,"$"+String.valueOf(curPrice),images,total_units,total_bought,price_threshold,unit_threshold,12.56,"16/11/2022 19:00",0.0,0.0)));
                        product_models.add(new AgoraProductModels("0",title,des,"$"+String.valueOf(curPrice),images,total_units,total_bought,price_threshold,unit_threshold,12.56,"16/11/2022 19:00",0.0,0.0));
                        Log.e("TEST", "Agora onResponse: " + title);
                        gridMainAdapter.notifyDataSetChanged();
                    }
                    jsonFlag = true;
                    if(backendFlag == true) la.stopAlertDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(MainPageActivity.this,LoginActivity.class));
            }
        });
    }

    public void mainPageClicked(View view) {
        if (view.getId() == R.id.account_TV) {
            // TEST CHAT HERE
//            Intent i = new Intent(MainPageActivity.this,showsellerMapActivity.class);
//            i.putExtra("p1",1.3461);
//            i.putExtra("p2",103.6814);
//            i.putExtra("p3",1.3421);
//            i.putExtra("p4",103.6816);
//            startActivity(i);

            Intent i = new Intent(MainPageActivity.this,Chat_Activity.class);
            i.putExtra("p1","1");
            i.putExtra("p2","Trump's Trumpet");
            i.putExtra("p3","S1000");
            i.putExtra("p4","https://i5.walmartimages.com/asr/ccc2ade5-e766-4773-adc3-3d5166ef48d5.56805f8fd63c7e7d6cb2040dd43e38df.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF");
            startActivity(i);

        }else if (view.getId() == R.id.logout_TV) {
            signOut();
        }else if (view.getId() == R.id.searchbar_button) {

            String searchterm = searchBar.getText().toString();
            if(searchterm.isEmpty()){
                prod_RV_title.setText("NEW ITEMS!");
                product_models.clear();
                product_models.addAll(g_search);
                gridMainAdapter.notifyDataSetChanged();
            }
            else{
                ArrayList<AgoraProductModels> gg = new ArrayList<>();
                int flag = 0;
                for(int i = 0; i < product_models.size();i++){
                    if(product_models.get(i).getProductName().toLowerCase().contains(searchterm.toLowerCase())){
                        flag++;
                        gg.add(product_models.get(i));
                        Log.e("TEST","FROM SEARCH "+product_models.get(i).getProductName());
                    }
                }
                if(flag > 0){
                    prod_RV_title.setText("Search Result For: "+searchterm);
                    product_models.clear();
                    product_models.addAll(gg);
                    gridMainAdapter.notifyDataSetChanged();
                }
                else{
                    prod_RV_title.setText("Search Results for: "+searchterm+" \nNot Found");
                }
            }
        }else if (view.getId() == R.id.manage_prod_IB){
            Intent i = new Intent(MainPageActivity.this,ManageProductActivity.class);
            startActivity(i);
        }else if (view.getId() == R.id.current_order_IB){
            Intent i = new Intent(MainPageActivity.this, CurrentOrderActivity.class);
            i.putExtra("p1",displayName);
            startActivity(i);
        }else if (view.getId() == R.id.like_prod_IB){
            Intent i = new Intent(MainPageActivity.this, FavItem_Activitiy.class);
            startActivity(i);
        }else if (view.getId() == R.id.Account_IB){
            Intent i = new Intent(MainPageActivity.this,AccountActivity.class);
            i.putExtra("NAME_USER",displayName);
            i.putExtra("NAME_EMAIL",email);
            i.putExtra("NAME_personID",personID);
            i.putExtra("NAME_URL",picurl.toString());
            startActivity(i);
        }
    }
    public void getProductsFromBackend() {
        la.startAlertDialog();
        HashMap<String, Object> map = new HashMap<>();
        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
        Log.e("TEST Agora Backend: ",authKey);

        map.put("authKey", authKey);
        map.put("cmd", "loadProducts");

        Call<BackEndArrayResource> call = api.getProductArray(map);
        call.enqueue(new Callback<BackEndArrayResource>() {
            @Override
            public void onResponse(@NonNull Call<BackEndArrayResource> call, @NonNull retrofit2.Response<BackEndArrayResource> response) {
                BackEndArrayResource resource = response.body();

                Log.e("TEST" , "Agora onResponse code LoadProducts: "+response.code());
                if(response.code() == 200){
                    Log.e("TEST" , "Agorra onResponse: LoadProducts: "+response.body().getStatus());
                    if(response.body().getStatus() == 0){
                        ArrayList<BackEndResource> backEndResources = new ArrayList<>(Arrays.asList(resource.getProductArray()));
                        Log.e("TEST Back End Agora Prod Size",String.valueOf(backEndResources.size()));

                        for(int i = 0; i <backEndResources.size() ;i++){
                            String prod_id= backEndResources.get(i).getProductId();
                            String prod_name = backEndResources.get(i).getName();
                            String prod_des = backEndResources.get(i).getDesc();
                            String[] prod_pics_base64 = backEndResources.get(i).getPics();
                            //TIME
                            Double sellerlong = backEndResources.get(i).getSellerLong();
                            Double sellerlat = backEndResources.get(i).getSellerLat();

                            int total_units =  backEndResources.get(i).getTotalUnits();
                            int total_bought =  backEndResources.get(i).getTotalBought();
                            float[] price_threshold =  backEndResources.get(i).getPriceThresholds();
                            int[] unit_threshold =  backEndResources.get(i).getUnitThresholds();
                            double distance = backEndResources.get(i).getDistanceFromUser();
                            String closingtime = backEndResources.get(i).getClosingTime();
                            float curPrice = price_threshold[0];
                            for(int p = 0; p < 4; p++){
                                if(total_bought>unit_threshold[p]){
                                    curPrice = price_threshold[p+1];
                                }
                            }
                            Log.e("TEST From Agora Backend", prod_name);

                            g_search.add((new AgoraProductModels(prod_id,prod_name,prod_des,"$"+String.format("%.2f",curPrice),prod_pics_base64,total_units,total_bought,price_threshold,unit_threshold,distance,closingtime,sellerlat,sellerlong)));
                            product_models.add(new AgoraProductModels(prod_id,prod_name,prod_des,"$"+String.format("%.2f",curPrice),prod_pics_base64,total_units,total_bought,price_threshold,unit_threshold,distance,closingtime,sellerlat,sellerlong));

                            // sort
                            Collections.sort(product_models,(o1, o2) -> o1.getDistancetoseller().compareTo(o2.getDistancetoseller()));
                            Collections.sort(g_search,(o1, o2) -> o1.getDistancetoseller().compareTo(o2.getDistancetoseller()));

                            gridMainAdapter.notifyDataSetChanged();
                            la.stopAlertDialog();
                        }
                    }
                    else{
                        Toast.makeText(MainPageActivity.this,"Error Please Try Again Later! \nStatus: "+response.body().getStatus(),Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(MainPageActivity.this,"Error With Backend! "+response.code(),Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<BackEndArrayResource> call, Throwable t) {
                Log.e("TEST", "onFailure: get hello :" + t.getMessage());
            }
        });
    }
    public void loadUserLatLongSP() {
        HashMap<String, Object> map = new HashMap<>();
        AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
        Log.e("TEST Backend: ",authKey);
        map.put("authKey", authKey);
        map.put("cmd", "viewAccount");
        Call<BackEndResource> call = api.getInfo(map);
        call.enqueue(new Callback<BackEndResource>() {
            @Override
            public void onResponse(Call<BackEndResource> call, retrofit2.Response<BackEndResource> response) {
                if(response.code() == 200){
                    if(response.body().getStatus() == 0){
                        SharedPreferences pref = getSharedPreferences("UserAuthKey", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("USERLAT",String.valueOf(response.body().getHomeLat()));
                        editor.putString("USERLONG",String.valueOf(response.body().getHomeLong()));
                        editor.commit();
                    }
                }
            }

            @Override
            public void onFailure(Call<BackEndResource> call, Throwable t) {
                la.stopAlertDialog();
            }
        });
    }

}