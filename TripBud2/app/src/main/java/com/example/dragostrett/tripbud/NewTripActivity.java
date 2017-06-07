package com.example.dragostrett.tripbud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class NewTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create New Trip");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finishAndRemoveTask ();
        }
        return super.onOptionsItemSelected(item);
    }

    public void createTrip(View view){
        EditText name=(EditText)findViewById(R.id.editText);
        TripInfo.setNameTrip(name.getText().toString());
        UserInfo.setTrip(name.getText().toString());
        EditText place=(EditText)findViewById(R.id.editText3);
        TripInfo.setPlace(place.getText().toString());
        TripInfo.setOrganizator(UserInfo.getUsername());
        TripInfo.setInATrip(true);
        new CreateTrip(this).execute();
        new CreateAnnouncement().execute();
        finish();
    }

}
