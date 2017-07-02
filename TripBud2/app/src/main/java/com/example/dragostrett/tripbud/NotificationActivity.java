package com.example.dragostrett.tripbud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dragostrett.tripbud.Background.AddUserBG;
import com.example.dragostrett.tripbud.Background.loginBG;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;

public class NotificationActivity extends AppCompatActivity {

    Button join, refuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Notifications");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if(!UserInfo.getNotification().equals("")){
            TextView v=(TextView)findViewById(R.id.textViewNotification);
            v.setText("Do you want to join "+UserInfo.getNotification()+"?");
            join=(Button)findViewById(R.id.button7);
            join.setVisibility(View.VISIBLE );
            refuse=(Button)findViewById(R.id.button9);
            refuse.setVisibility(View.VISIBLE );
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finishAndRemoveTask ();
        }
        return super.onOptionsItemSelected(item);
    }

    public void join(View view){
        new AddUserBG(this).execute("1");
        UserInfo.setTrip(UserInfo.getNotification());
        new loginBG(this).execute(UserInfo.getUsername(), UserInfo.getPassword());
        MainActivity.cont.finishAndRemoveTask();
        this.finishAndRemoveTask ();
    }
    public void refuse(View view){
        UserInfo.setNotification("");
        new AddUserBG(this).execute("0");
        this.finishAndRemoveTask ();
    }

}
