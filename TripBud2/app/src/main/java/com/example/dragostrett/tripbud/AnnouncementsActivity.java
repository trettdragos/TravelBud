package com.example.dragostrett.tripbud;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dragostrett.tripbud.Background.AnnouncementsBG;
import com.example.dragostrett.tripbud.Background.addAnnouncementBG;
import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;

public class AnnouncementsActivity extends AppCompatActivity {
    EditText m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Announcements");
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                   //     .setAction("Action", null).show();
            }
        });
        View linearLayout =  findViewById(R.id.announcements);
        Button n=(Button)findViewById(R.id.setNewNews);
        m=(EditText)findViewById(R.id.newNews);
        if(UserInfo.getUsername().equals(TripInfo.getOrganizator())){
            n.setVisibility(View.VISIBLE);
            m.setVisibility(View.VISIBLE);
        }
        new AnnouncementsBG(this, linearLayout).execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            MainActivity.start();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void sendAnouncement(View view){
        if(!m.getText().toString().equals("")){
            new addAnnouncementBG().execute(m.getText().toString());
            m.setText("");
            this.recreate();
        }

        Snackbar.make(view, "sent", Snackbar.LENGTH_LONG)
             .setAction("Action", null).show();
    }
}
