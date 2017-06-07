package com.example.dragostrett.tripbud;

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

public class MeetingActivity extends AppCompatActivity {

    Button loc, delete;
    EditText timeEdit;
    TextView time;
    EditText date;
    EditText time2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Meeting Today");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        timeEdit=(EditText)findViewById(R.id.editText_time);
        time=(TextView)findViewById(R.id.textView_time);
        if(UserInfo.getType().equals("1")){
            if(TripInfo.getMeet().equals("")){
                loc=(Button)findViewById(R.id.button_place);
                loc.setVisibility(View.VISIBLE);
                //timeEdit.setVisibility(View.VISIBLE);
                date=(EditText)findViewById(R.id.editText4);
                time2=(EditText)findViewById(R.id.editText2);
                date.setVisibility(View.VISIBLE);
                time2.setVisibility(View.VISIBLE);
            }
            else{
                time.setVisibility(View.VISIBLE);
                time.setText(TripInfo.getMeet());
                delete=(Button)findViewById(R.id.button_delete);
                delete.setVisibility(View.VISIBLE);
            }
        }else{
            time.setVisibility(View.VISIBLE);
            if(TripInfo.getMeet().equals(""))
                time.setText("Not set yet");
            else time.setText(TripInfo.getMeet());
        }
    }
    public void createMeeting(View view){
        if(time2.getText().toString().equals("") || date.getText().toString().equals(""))
            Toast.makeText(this, "Please chose a time",
                    Toast.LENGTH_SHORT).show();
        else{
            TripInfo.setMeet(date.getText().toString()+"\n"+time2.getText().toString());
            Intent intent = new Intent(this, ChooseLocationActivity.class);
            startActivity(intent);
            this.finishAndRemoveTask();
        }

    }
    public void deleteMeet(View view){
        new  DeleteMeet(this).execute();
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finishAndRemoveTask ();
        }
        return super.onOptionsItemSelected(item);
    }
}
