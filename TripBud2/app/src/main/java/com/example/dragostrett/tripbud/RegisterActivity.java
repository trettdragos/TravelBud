package com.example.dragostrett.tripbud;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    EditText username, pass1, pass2, email;
    public static Activity fa;
    public static boolean admin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fa=this;
        MainActivity.k=true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("Register");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        username=(EditText)findViewById(R.id.editText_username);
        email=(EditText)findViewById(R.id.editText_email);
        pass1=(EditText)findViewById(R.id.editText_password);
        pass2=(EditText)findViewById(R.id.editText_passwordCheck);
    }
    public void registerUser(View view){
        String name=username.getText().toString(), mail=email.getText().toString(), pas1=pass1.getText().toString(), pas2=pass2.getText().toString();
        if(SecurityCheck.isUsernameOk(name) && SecurityCheck.isEmailOk(mail).equals("") && SecurityCheck.isPasswordOk(pas1).equals("") && pas1.equals(pas2) ){
            MainActivity.k=false;
            CheckBox b=(CheckBox)findViewById(R.id.checkBox);
            if(b.isChecked())
                admin=true;
            new RegisterBG(this).execute(name, pas1, mail);
        }
        else {
            if(!pas1.equals(pas2))
                Toast.makeText(this, "Passwords don't match",
                        Toast.LENGTH_SHORT).show();
            else{
                int nr=name.length();
                String s="";
                if(nr<5)
                    s="Username not long enough";
                Toast.makeText(this, SecurityCheck.isEmailOk(mail)+SecurityCheck.isPasswordOk(pas1)+s,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finishAndRemoveTask ();
        }
        return super.onOptionsItemSelected(item);
    }
    GoogleApiClient mGoogleApiClient;
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            UserInfo.setLocation(true);
            UserInfo.setLatitudine(String.valueOf(mLastLocation.getLatitude()));
            UserInfo.setLongitudine(String.valueOf(mLastLocation.getLongitude()));
        } else {Toast.makeText(this, "Location Failure",
                Toast.LENGTH_SHORT).show();
            UserInfo.setLocation(false);}
    }   

    @Override
    public void onConnectionSuspended(int i) {

    }
}
