package com.example.dragostrett.tripbud;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dragostrett.tripbud.Background.CreateAnnouncement;
import com.example.dragostrett.tripbud.Background.CreateTrip;
import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;

import java.sql.Date;
import java.util.Calendar;

public class NewTripActivity extends AppCompatActivity {

    public Button secondDateButton;
    public static final int DIALOG_ID=45;
    public static final int DIALOG_ID2=54;
    int yearStart, monthStart, dayStart;
    int yearEnd, monthEnd, dayEnd;
    int curentYear, curentMonth, curentDay;
    public static Calendar cal;
    public TextView endDate, startDate;
    public Button firstButton;

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
        secondDateButton = (Button)findViewById(R.id.buttonSecondDatePicker);
        firstButton = (Button)findViewById(R.id.buttonStartDate);
        cal= Calendar.getInstance();
        yearStart=cal.get(Calendar.YEAR);
        monthStart=cal.get(Calendar.MONTH);
        dayStart=cal.get(Calendar.DAY_OF_MONTH);
        yearEnd=cal.get(Calendar.YEAR);
        monthEnd=cal.get(Calendar.MONTH);
        dayEnd=cal.get(Calendar.DAY_OF_MONTH);
        curentYear=cal.get(Calendar.YEAR);
        curentMonth=cal.get(Calendar.MONTH);
        curentDay=cal.get(Calendar.DAY_OF_MONTH);
        startDate=(TextView) findViewById(R.id.textViewStartDate);
        endDate=(TextView)findViewById(R.id.textViewEndDate);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            MainActivity.start();
            this.finishAndRemoveTask ();
        }
        return super.onOptionsItemSelected(item);
    }

    public void openDatePicker(View view){
        showDialog(DIALOG_ID);
    }
    public void openSecondDatePicker(View view){
        showDialog(DIALOG_ID2);
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id==45){
            return new DatePickerDialog(this, datePickierListenerStart, yearStart, monthStart, dayStart);
        }if(id==54){
            return new DatePickerDialog(this, datePickierListenerEnd, yearEnd, monthEnd, dayEnd);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickierListenerStart = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if(year<yearEnd){
                Toast.makeText(NewTripActivity.this,"Please choose a future date", Toast.LENGTH_SHORT).show();
            }else if(month<monthEnd-1){
                Toast.makeText(NewTripActivity.this,"Please choose a future date", Toast.LENGTH_SHORT).show();
            }else if(dayOfMonth<dayEnd){
                Toast.makeText(NewTripActivity.this,"Please choose a future date", Toast.LENGTH_SHORT).show();
            }else {
                yearStart=year;
                monthStart=month+1;
                dayStart=dayOfMonth;
                startDate.setText(dayStart+"-"+monthStart+"-"+yearStart);
                secondDateButton.setVisibility(View.VISIBLE);
                firstButton.setVisibility(View.INVISIBLE);
                Toast.makeText(NewTripActivity.this,"Please choose an ending date", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private DatePickerDialog.OnDateSetListener datePickierListenerEnd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if(year<yearStart){
                Toast.makeText(NewTripActivity.this,"Please choose a future date", Toast.LENGTH_SHORT).show();
            }else if(month<monthStart-1){
                Toast.makeText(NewTripActivity.this,"Please choose a future date", Toast.LENGTH_SHORT).show();
            }else if(dayOfMonth<dayStart){
                Toast.makeText(NewTripActivity.this,"Please choose a future date", Toast.LENGTH_SHORT).show();
            }else{
                yearEnd=year;
                monthEnd=month+1;
                dayEnd=dayOfMonth;
                endDate.setText(dayEnd+"-"+monthEnd+ "-"+yearEnd);
                //Toast.makeText(NewTripActivity.this,"Period set", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void createTrip(View view){
        EditText name=(EditText)findViewById(R.id.editText);
        EditText place=(EditText)findViewById(R.id.editText3);
        if(!name.getText().equals("") && !place.getText().equals("") && !startDate.getText().equals("Start Date") && !endDate.getText().equals("End Date")){
            TripInfo.setNameTrip(name.getText().toString());
            UserInfo.setTrip(name.getText().toString());
            TripInfo.setPlace(place.getText().toString());
            TripInfo.setOrganizator(UserInfo.getUsername());
            TripInfo.setInATrip(true);
            Date end=new Date(yearEnd-1900, monthEnd-1, dayEnd);
            TripInfo.setEndDate(end);
            end=new Date(yearStart-1900, monthStart-1, dayStart);
            //Toast.makeText(this, end.toString()+"   " +yearEnd, Toast.LENGTH_SHORT).show();
            TripInfo.setStartDate(end);
            new CreateTrip(this).execute();
            new CreateAnnouncement().execute();
            this.finish();
        }else Toast.makeText(this, "Please fill in all spaces and dates", Toast.LENGTH_SHORT).show();
    }

}
