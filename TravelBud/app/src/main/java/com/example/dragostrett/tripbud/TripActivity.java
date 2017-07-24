package com.example.dragostrett.tripbud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dragostrett.tripbud.Background.DeleteTripBG;
import com.example.dragostrett.tripbud.Background.RemoveUserBG;
import com.example.dragostrett.tripbud.Background.SendJoinNotif;
import com.example.dragostrett.tripbud.Background.UpdateCircle;
import com.example.dragostrett.tripbud.Background.UpdateUserInfo;
import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

public class TripActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, SeekBar.OnSeekBarChangeListener {

    TextView trip, place, organizator, time;
    EditText newUserToTrip, userName;
    Button seeAll, addnew, delete, create, deleteUser;
    public static Activity tripAc;
    public static SeekBar seekBar;
    public static TextView textViewProgres;
    static int prog=0;
    String radius ="Please choose the radius: ";
    public static LatLng temporarLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tripAc=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Current Trip");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        userName = (EditText)findViewById(R.id.editText_deleteUser);
        deleteUser = (Button)findViewById(R.id.button_deleteUser);
        if(TripInfo.isInATrip()){//check if user is in a trip and display information acordingly\
            trip=(TextView)findViewById(R.id.textView_trip);
            trip.setText("Name: "+TripInfo.getNameTrip());
            place=(TextView)findViewById(R.id.textView_place);
            place.setText("Location: "+TripInfo.getPlace());
            organizator=(TextView)findViewById(R.id.textView_organizator);
            organizator.setText("Organizator: "+TripInfo.getOrganizator());
            time=(TextView)findViewById(R.id.textViewPeriod);
            time.setText("Starting from:"+TripInfo.getStartDate()+"\n"+"Ending on:"+TripInfo.getEndDate());
            time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            trip.setVisibility(View.VISIBLE);
            place.setVisibility(View.VISIBLE);
            organizator.setVisibility(View.VISIBLE);
            time.setVisibility(View.VISIBLE);
            seeAll=(Button)findViewById(R.id.button3);
            seeAll.setVisibility(View.VISIBLE);
            delete=(Button)findViewById(R.id.button4);
            if(UserInfo.getType().equals("1")){//check is user is admin to give him admin acces
                Button range=(Button)findViewById(R.id.buttonRange);
                range.setVisibility(View.VISIBLE);
                Button dRange=(Button)findViewById(R.id.button_delete_range);
                dRange.setVisibility(View.VISIBLE);
                newUserToTrip=(EditText)findViewById(R.id.editText_username);
                newUserToTrip.setVisibility(View.VISIBLE);
                addnew=(Button)findViewById(R.id.button5);
                addnew.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                userName.setVisibility(View.VISIBLE);
                deleteUser.setVisibility(View.VISIBLE);
            }
            else{
                delete.setText("Exit Trip");
                delete.setVisibility(View.VISIBLE);
            }
        }else{//display that there is no trip
            TextView erro=(TextView)findViewById(R.id.textView4);
            erro.setVisibility(View.VISIBLE);
            if(UserInfo.getType().equals("1")){//if user is admin he can create a new trip
                create=(Button)findViewById(R.id.button6);
                create.setVisibility(View.VISIBLE);
            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            MainActivity.start();
            this.finishAndRemoveTask ();
        }
        return super.onOptionsItemSelected(item);
    }
    public void seeUsers(View view){
        //see list of all users in the current trip
        Intent intent = new Intent(this, UsersActivity.class);
        this.startActivity(intent);
    }
    public void addUser(View view){
        //add new user to the trip
        if(!newUserToTrip.getText().toString().equals("")){
            //new AddUserBG(this).execute(newUserToTrip.getText().toString());
            new SendJoinNotif(this).execute(newUserToTrip.getText().toString());
            newUserToTrip.setText("");
        }
        else{
            Toast.makeText(this, "Please enter a name",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteTrip(View view){
        if(UserInfo.getType().equals("1")){
            //delete the trip
            if(!TripInfo.getMeet().equals(""))
                MainActivity.meet.remove();
            new DeleteTripBG(this).execute();
            finish();
        } else{
            String aux="0";
            if(UserInfo.isVisible())
                aux="1";
            new UpdateUserInfo(this).execute(UserInfo.getUsername(), UserInfo.getPassword(), UserInfo.getEmail(), aux, "");
            finishAndRemoveTask();
        }

    }
    public void createTrip(View view){
        //create new trip

        Intent intent = new Intent(this, NewTripActivity.class);
        this.startActivity(intent);
        this.finishAndRemoveTask();
    }
    public void removeUser(View view){
        //remove user from trip
        if(!userName.getText().toString().equals("")){
            new RemoveUserBG(this).execute(userName.getText().toString());
            userName.setText("");
        }
        else{
            Toast.makeText(this, "Please enter a name",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void editRange(View view){
        //Intent intent = new Intent(this, ChooseRangeCenterActivity.class);
        //this.startActivity(intent);
        GoogleApiClient mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        //this.finishAndRemoveTask();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);
            temporarLoc=place.getLatLng();
            //String toastMsg = String.format("Place: %s", place.getName());
            //Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            setContentView(R.layout.radius_dialog);
            seekBar = (SeekBar)findViewById(R.id.seekBar3);
            seekBar.setMax(90);
            seekBar.setOnSeekBarChangeListener(this);
            getSupportActionBar().hide();
            textViewProgres = (TextView)findViewById(R.id.textViewProgres);
        }

    }
    public void deleteRange(View view){
        TripInfo.setCircleRange(0);
        new UpdateCircle(this).execute();
        //this.finishAndRemoveTask();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

    public void onCancel(View view){
        this.finishAndRemoveTask();
    }

    public void onOK(View view){
        TripInfo.setCircleCenter(temporarLoc);
        TripInfo.setCircleRange(prog*1000);
        new UpdateCircle(this).execute();
        this.finishAndRemoveTask();
    }
}
