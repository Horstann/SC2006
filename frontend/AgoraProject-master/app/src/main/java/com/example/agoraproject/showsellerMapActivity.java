package com.example.agoraproject;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.agoraproject.databinding.ActivityShowsellerMapBinding;
import com.squareup.picasso.Picasso;

public class showsellerMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityShowsellerMapBinding binding;
    double userlat;         // Product Id
    double userlong;             // Product Name
    double sellerlat;        // Product Price
    double sellerlong;
//    LatLng user = new LatLng(1.3461,103.6814);
//    LatLng seller = new LatLng(1.3421,103.6816);
    LatLng user;
    LatLng seller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intCollect = getIntent();
        Bundle b = intCollect.getExtras();
        if(b!= null) {
            userlat = b.getDouble("p1");         // Product Id
            userlong = b.getDouble("p2");             // Product Name
            sellerlat = b.getDouble("p3");            // Product Price
            sellerlong =b.getDouble("p4");


            if(userlat == sellerlat || userlong==sellerlong){
                userlat+=0.0002;
                userlong+=0.0001;
            }
            user = new LatLng(userlat,userlong);
            seller = new LatLng(sellerlat,sellerlong);
            Log.e("TEST",sellerlat+"");
        }
        Log.e("TEST",user+"");


        binding = ActivityShowsellerMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(user).title("Your Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.addMarker(new MarkerOptions().position(seller).title("Seller Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seller , 15.0f));

    }
}