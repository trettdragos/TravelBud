package com.example.dragostrett.tripbud;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dragostrett.tripbud.Background.DeleteMeet;
import com.example.dragostrett.tripbud.Background.NewMeet;
import com.example.dragostrett.tripbud.BasicInfo.MeetInfo;
import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

public class MeetingActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button loc, delete;
    TextView time, textViewDate;
    Button buttonSetDate, buttonSetHour;
    int DIALOG_ID_DATE=57;
    int DIALOG_ID_HOUR=39;
    int yearP, monthP, dayP, hourP, minuteP;
    int curentYear, curentMonth, curentDay, curentHour, curentMinute;
    Date date=null;
    Time timeOfDate = null;
    Calendar cal;
    boolean isSameDay = false;

    public static String untilConfirmSave="";

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
        buttonSetDate=(Button)findViewById(R.id.buttonSetDate);
        buttonSetHour=(Button)findViewById(R.id.buttonSetHour);
        textViewDate = (TextView)findViewById(R.id.textViewDate);
        time=(TextView)findViewById(R.id.textView_time);
        textViewDate.setVisibility(View.INVISIBLE);
        loc= (Button) findViewById(R.id.button_place);
        if(UserInfo.getType().equals("1")){
            if(TripInfo.getMeet().equals("")){
                //no meet, yet the user is admin
                loc=(Button)findViewById(R.id.button_place);
                //timeEdit.setVisibility(View.VISIBLE);
                buttonSetDate.setVisibility(View.VISIBLE);
                //buttonSetHour.setVisibility(View.VISIBLE);
                textViewDate.setVisibility(View.VISIBLE);
                time.setVisibility(View.VISIBLE);
                textViewDate.setText("Date");
                textViewDate.setVisibility(View.VISIBLE);
                time.setText("Time");
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

        cal= Calendar.getInstance();
        yearP=cal.get(Calendar.YEAR);
        monthP=cal.get(Calendar.MONTH);
        dayP=cal.get(Calendar.DAY_OF_MONTH);
        hourP=cal.get(Calendar.HOUR);
        minuteP=cal.get(Calendar.MINUTE);
        curentYear=cal.get(Calendar.YEAR);
        curentMonth=cal.get(Calendar.MONTH);
        curentDay=cal.get(Calendar.DAY_OF_MONTH);
        curentHour=cal.get(Calendar.HOUR_OF_DAY);
        curentMinute=cal.get(Calendar.MINUTE);
    }

    public void setDate(View view){showDialog(DIALOG_ID_DATE);}
    public void setHour(View view){showDialog(DIALOG_ID_HOUR);}

    @Override
    protected Dialog onCreateDialog(int id){
        if(id==DIALOG_ID_DATE){
            return new DatePickerDialog(this, datePickierListener, yearP, monthP, dayP);
        }if(id==DIALOG_ID_HOUR){
            return new TimePickerDialog(this, timePickerListener, hourP, minuteP, false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickierListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yearP=year;
            monthP=month;
            dayP=dayOfMonth;

            if(year==curentYear && month==curentMonth && dayOfMonth==curentDay){
                isSameDay=true;
            }

            if(year<curentYear){
                Toast.makeText(MeetingActivity.this, "please choose a future date", Toast.LENGTH_LONG).show();
            }else if(month<curentMonth){
                Toast.makeText(MeetingActivity.this, "please choose a future date", Toast.LENGTH_LONG).show();
            }else if(dayOfMonth<curentDay){
                Toast.makeText(MeetingActivity.this, "please choose a future date", Toast.LENGTH_LONG).show();
            }else{
                date = new Date(year-1900, month, dayOfMonth);
                buttonSetDate.setVisibility(View.INVISIBLE);
                textViewDate.setText(date.toString());
                buttonSetHour.setVisibility(View.VISIBLE);
            }
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Log.e("time checking", hourOfDay+ " " + minute + " : "+curentHour+ " "+ curentMinute);
            if(isSameDay){
                if(hourOfDay<curentHour){
                    Toast.makeText(MeetingActivity.this, "please choose a future time", Toast.LENGTH_LONG).show();
                }else if(minute<curentMinute){
                    Toast.makeText(MeetingActivity.this, "please choose a future time", Toast.LENGTH_LONG).show();
                }else{
                    timeOfDate = new Time(hourOfDay, minute, 0);
                    buttonSetHour.setVisibility(View.INVISIBLE);
                    time.setText(timeOfDate.toString());
                    loc.setVisibility(View.VISIBLE);
                }
            }else{
                timeOfDate = new Time(hourOfDay, minute, 0);
                buttonSetHour.setVisibility(View.INVISIBLE);
                time.setText(timeOfDate.toString());
                loc.setVisibility(View.VISIBLE);
            }

        }
    };

    public void createMeeting(View view){
        if(timeOfDate.equals(null) || date.equals(null))
            Toast.makeText(this, "Please chose a time",
                    Toast.LENGTH_SHORT).show();
        else{
            //TripInfo.setMeet(date.getText().toString()+"\n"+time2.getText().toString());
            untilConfirmSave=date.toString()+":"+timeOfDate.toString();
            //Intent intent = new Intent(this, ChooseLocationActivity.class);
            //startActivity(intent);
            //this.finishAndRemoveTask();
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
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);
            MeetInfo.setMoment(untilConfirmSave);
            TripInfo.setMeet(untilConfirmSave);
            MeetInfo.setPosition(place.getLatLng());
            MainActivity.meet = MainActivity.mMap.addMarker(new MarkerOptions().position(MeetInfo.getPosition()).title(TripInfo.getMeet()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            new NewMeet(this).execute(MeetingActivity.untilConfirmSave, String.valueOf(MeetInfo.getPosition().latitude), String.valueOf(MeetInfo.getPosition().longitude));
            this.finishAndRemoveTask();
        }

    }

    public void deleteMeet(View view){
        new DeleteMeet(this).execute();
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            MainActivity.start();
            this.finishAndRemoveTask ();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
