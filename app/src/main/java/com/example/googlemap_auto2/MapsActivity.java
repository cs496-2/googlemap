package com.example.googlemap_auto2;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.googlemap_auto2.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ArrayList<LatLng> locationArrayList;
    private ArrayList<String> scheduleArrayList;

    Geocoder geocoder;
    List<Address> addressList = null;
    Address address;
    LatLng addedLatLng;
    Marker addedMarker;
    Marker prevMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Start!");

        scheduleArrayList = new ArrayList<>();
        scheduleArrayList.add("런던");
        scheduleArrayList.add("맨체스터");
        scheduleArrayList.add("브리스톨");
        scheduleArrayList.add("파리");
        scheduleArrayList.add("베를린");
        scheduleArrayList.add("Munchen");

         geocoder= new Geocoder(MapsActivity.this);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        System.out.println("check1");
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        System.out.println("chck2");
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

        LatLngBounds.Builder builder = new LatLngBounds.Builder();  //


        for(int i = 0; i < scheduleArrayList.size();i++){
            try {
                addressList = geocoder.getFromLocationName(scheduleArrayList.get(i), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            address = addressList.get(0);
            addedLatLng = new LatLng(address.getLatitude(), address.getLongitude());
            prevMarker = addedMarker;
            addedMarker = mMap.addMarker(new MarkerOptions().position(addedLatLng).title(scheduleArrayList.get(i)));

            builder.include(addedLatLng);   //

            addedMarker.setIcon(BitmapFromVector(getApplicationContext(),R.drawable.ic_baseline_where_to_vote_24));
            if( i != 0 ){
                mMap.addPolyline(new PolylineOptions()
                        .add(prevMarker.getPosition(), addedMarker.getPosition())
                        .width(25).pattern(Arrays.asList(
                                new Dot(), new Gap(20), new Dash(30), new Gap(20)))
                        .color(Color.BLUE)
                        .geodesic(true));

            }
        }

        LatLngBounds bounds = builder.build();

        mMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(addedMarker.getPosition()));
        int padding = 0;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 360, 760, 0);
        mMap.moveCamera(cu);




/*
        locationArrayList = new ArrayList<>();
        System.out.println("prob0...");
//        LatLngBounds.builder

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng TamWorth = new LatLng(-31.083332, 150.916672);
        LatLng NewCastle = new LatLng(-32.916668, 151.750000);
        LatLng Brisbane = new LatLng(-27.470125, 153.021072);
        System.out.println("prob1");
        locationArrayList.add(TamWorth);
        locationArrayList.add(sydney);
        locationArrayList.add(NewCastle);
        locationArrayList.add(Brisbane);
        System.out.println("builder probe");
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(int i = 0; i < locationArrayList.size(); i++){
            System.out.println("hahaha");
//            builder.include(locationArrayList.get(i));
            mMap.addMarker((new MarkerOptions().position(locationArrayList.get(i)).title("Marker")));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
            if( i != 0 ){
                System.out.println("polyline adding");
                mMap.addPolyline(new PolylineOptions()
                        .add(locationArrayList.get(i-1), locationArrayList.get(i))
                        .width(10)
                        .color(Color.BLUE)
                        .geodesic(true));

            }
//            else{
//                mMap.addPolyline(new PolylineOptions().add(locationArrayList.get(locationArrayList.size()-1), locationArrayList.get(0)).width(10).color(Color.BLUE).geodesic(true));
//            }

        }
//        LatLngBounds bounds = builder.build();
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 25, 25, 5);
//        googleMap.moveCamera(cu);


*/
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // on marker click we are getting the title of our marker
                // which is clicked and displaying it in a toast message.
                String markerName = marker.getTitle();
                Toast.makeText( MapsActivity.this, "Clicked location is " + markerName, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney")).setIcon(BitmapFromVector(getApplicationContext(),R.drawable.ic_baseline_calendar_today_24));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.addMarker(new MarkerOptions().position(TamWorth).title("Marker in TamWorth")).setIcon(BitmapFromVector(getApplicationContext(),R.drawable.ic_baseline_calendar_today_24));

        // inside on map ready method
        // we will be displaying polygon on Google Maps.
        // on below line we will be adding polyline on Google Maps.
//        mMap.addPolyline((new PolylineOptions()).add(Brisbane, NewCastle, TamWorth).
//                // below line is use to specify the width of poly line.
//                        width(5)
//                // below line is use to add color to our poly line.
//                .color(Color.RED)
//                // below line is to make our poly line geodesic.
//                .geodesic(true));
        // on below line we will be starting the drawing of polyline.
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TamWorth, 13));
    }


    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}