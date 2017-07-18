package com.example.dragostrett.tripbud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dragostrett.tripbud.Background.DeleteTripBG;
import com.example.dragostrett.tripbud.Background.RemoveUserBG;
import com.example.dragostrett.tripbud.Background.SendJoinNotif;
import com.example.dragostrett.tripbud.Background.UpdateCircle;
import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;

public class TripActivity extends AppCompatActivity {

    TextView trip, place, organizator, time;
    EditText newUserToTrip, userName;
    Button seeAll, addnew, delete, create, deleteUser;
    public static Activity tripAc;
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
            if(UserInfo.getType().equals("1")){//check is user is admin to give him admin acces
                Button range=(Button)findViewById(R.id.buttonRange);
                range.setVisibility(View.VISIBLE);
                Button dRange=(Button)findViewById(R.id.button_delete_range);
                dRange.setVisibility(View.VISIBLE);
                newUserToTrip=(EditText)findViewById(R.id.editText_username);
                newUserToTrip.setVisibility(View.VISIBLE);
                addnew=(Button)findViewById(R.id.button5);
                addnew.setVisibility(View.VISIBLE);
                delete=(Button)findViewById(R.id.button4);
                delete.setVisibility(View.VISIBLE);
                userName.setVisibility(View.VISIBLE);
                deleteUser.setVisibility(View.VISIBLE);
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
        //delete the trip
        if(!TripInfo.getMeet().equals(""))
            MainActivity.meet.remove();
        new DeleteTripBG(this).execute();
        finish();
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
        Intent intent = new Intent(this, ChooseRangeCenterActivity.class);
        this.startActivity(intent);
        this.finishAndRemoveTask();
    }
    public void deleteRange(View view){
        TripInfo.setCircleRange(0);
        new UpdateCircle(this).execute();
        //this.finishAndRemoveTask();
    }
}
