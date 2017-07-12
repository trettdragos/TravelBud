package com.example.dragostrett.tripbud;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dragostrett.tripbud.Background.UpdateCircle;
import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ChooseRangeCenterActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, SeekBar.OnSeekBarChangeListener {

    GoogleMap MMap;
    public static LatLng lastLoc = UserInfo.userLoc;
    public static AlertDialog.Builder builder;
    public static SeekBar seekBar;
    public static TextView textViewProgres;
    public Activity context;
    static int prog;
    MarkerOptions centre;
    String radius ="Please choose the radius: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.stop();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_range_center);
        context=this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportActionBar().setTitle("Pick one location");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MMap = googleMap;
        MMap.setOnMarkerDragListener(this);
        centre = new MarkerOptions().position(UserInfo.getUserLoc()).title(UserInfo.getUsername());
        centre.draggable(true);
        MMap.addMarker(centre);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(UserInfo.getUserLoc())       // Sets the center of the map to location user
                .zoom(13)// Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        MMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rangecancel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            this.finishAndRemoveTask ();
        }else if (id == R.id.action_next) {
            setContentView(R.layout.radius_dialog);
            seekBar = (SeekBar)findViewById(R.id.seekBar3);
            seekBar.setMax(90);
            seekBar.setOnSeekBarChangeListener(this);
            getSupportActionBar().hide();
            textViewProgres = (TextView)findViewById(R.id.textViewProgres);
            //setContentView(R.layout.activity_choose_range_center);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        lastLoc=marker.getPosition();
        Toast.makeText(this, String.valueOf(marker.getPosition()), Toast.LENGTH_SHORT).show();
    }

    public void onCancel(View view){
        context.finishAndRemoveTask();
    }

    public void onOK(View view){
        TripInfo.setCircleCenter(lastLoc);
        TripInfo.setCircleRange(prog*1000);
        Toast.makeText(this, String.valueOf(TripInfo.getCircleCenter()), Toast.LENGTH_SHORT).show();
        //TODO update DB
        new UpdateCircle(this).execute();
        this.finishAndRemoveTask();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        prog=progress/10+1;
        textViewProgres.setText(radius+String.valueOf(progress/10+1)+"km");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

