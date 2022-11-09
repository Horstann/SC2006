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
import com.example.agoraproject.databinding.ActivityShowProductQueuemapBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class showProductQueuemap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityShowProductQueuemapBinding binding;
    ArrayList<Double> fLat = new ArrayList<>();
    ArrayList<Double> fLong = new ArrayList<>();
    ArrayList<LatLng> distances = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intCollect = getIntent();
        Bundle b = intCollect.getExtras();
        if(b!= null) {
            ArrayList<Double> lat = (ArrayList<Double>) getIntent().getSerializableExtra("Lat");
            ArrayList<Double> longg = (ArrayList<Double>) getIntent().getSerializableExtra("Long");
            for(int i = 0; i < lat.size();i++){
                distances.add(new LatLng(lat.get(i),longg.get(i)));
            }
        }

        binding = ActivityShowProductQueuemapBinding.inflate(getLayoutInflater());
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

       for(int i = 0; i < distances.size();i++){
           if(i == distances.size()-1){
               mMap.addMarker(new MarkerOptions().position(distances.get(i)).title("Seller Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
           }
           else if(i == 1){
               mMap.addMarker(new MarkerOptions().position(distances.get(i)).title("Item Collection Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
           }
           else{
               mMap.addMarker(new MarkerOptions().position(distances.get(i)).title("Buyer "+String.valueOf(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

           }
           // below lin is use to zoom our camera on map.
           mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

           // below line is use to move our camera to the specific location.
           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(distances.get(i), 15.0f));
       }
    }
}