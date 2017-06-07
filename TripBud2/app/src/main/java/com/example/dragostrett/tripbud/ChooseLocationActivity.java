package com.example.dragostrett.tripbud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ChooseLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    MarkerOptions shit;
    String latitudine="";
    String longitudine="";
    GoogleMap mmMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Press Back To Confirm");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(latitudine.equals("")){
                Toast.makeText(this, "Please chose a location before exiting",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                MeetInfo.setLatitudine(latitudine);
                MeetInfo.setLongitudine(longitudine);
                LatLng sydne = new LatLng(Double.parseDouble(MeetInfo.getLatitudine().toString()), Double.parseDouble(MeetInfo.getLongitudine().toString()));
                MainActivity.meet = MainActivity.mMap.addMarker(new MarkerOptions().position(sydne).title(TripInfo.getMeet()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                new NewMeet(this).execute();
                Toast.makeText(this, "Meeting Point created",
                        Toast.LENGTH_SHORT).show();
                this.finishAndRemoveTask ();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }
    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng dragPosition = marker.getPosition();
        double dragLat = dragPosition.latitude;
        double dragLong = dragPosition.longitude;
        latitudine = Double.toString(dragLat);
        longitudine = Double.toString(dragLong);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mmMap = googleMap;
        mmMap.setOnMarkerDragListener(this);
        LatLng sydney = new LatLng(Double.parseDouble(UserInfo.getLatitudine().toString()), Double.parseDouble(UserInfo.getLongitudine().toString()));
        shit = new MarkerOptions().position(sydney).title("Location").draggable(true);
        mmMap.addMarker(shit);
        mmMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mmMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(UserInfo.getLatitudine()), Double.parseDouble(UserInfo.getLongitudine())), 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.parseDouble(UserInfo.getLatitudine()), Double.parseDouble(UserInfo.getLongitudine())))       // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mmMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}
