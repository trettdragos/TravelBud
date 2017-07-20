package com.example.dragostrett.tripbud;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Trett Dragos
 * Clasa speciala pentru testat noi functionalitati
 */
public class TestingActivity extends AppCompatActivity {

    public static final int DIALOG_ID=44;
    public static final int DIALOG_ID2=53;
    int yearStart, monthStart, dayStart;
    int yearEnd, monthEnd, dayEnd;
    Button choseEnd;
    public static Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        cal= Calendar.getInstance();
        yearStart=cal.get(Calendar.YEAR);
        monthStart=cal.get(Calendar.MONTH);
        dayStart=cal.get(Calendar.DAY_OF_MONTH);
        yearEnd=cal.get(Calendar.YEAR);
        monthEnd=cal.get(Calendar.MONTH);
        dayEnd=cal.get(Calendar.DAY_OF_MONTH);
        choseEnd=(Button)findViewById(R.id.buttonEnd);

    }
    public void openDatePicker(View view){
        showDialog(DIALOG_ID);
    }
    public void openSecond(View view){
        showDialog(DIALOG_ID2);
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id==44){
            return new DatePickerDialog(this, datePickierListenerStart, yearStart, monthStart, dayStart);
        }if(id==53){
            return new DatePickerDialog(this, datePickierListenerEnd, yearEnd, monthEnd, dayEnd);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickierListenerStart = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yearStart=year;
            monthStart=month+1;
            dayStart=dayOfMonth;
            if(yearStart<yearEnd){
                Toast.makeText(TestingActivity.this,"Please choose a future date", Toast.LENGTH_SHORT).show();
            }else if(monthStart<monthEnd){
                Toast.makeText(TestingActivity.this,"Please choose a future date", Toast.LENGTH_SHORT).show();
            }else if(dayStart<dayEnd){
                Toast.makeText(TestingActivity.this,"Please choose a future date", Toast.LENGTH_SHORT).show();
            }else {
                choseEnd.setVisibility(View.VISIBLE);
                Toast.makeText(TestingActivity.this,"Please choose an ending date", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private DatePickerDialog.OnDateSetListener datePickierListenerEnd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yearEnd=year;
            monthEnd=month+1;
            dayEnd=dayOfMonth;
            if(yearEnd<yearStart){
                Toast.makeText(TestingActivity.this,"Please choose a future date", Toast.LENGTH_SHORT).show();
            }else if(monthEnd<monthStart){
                Toast.makeText(TestingActivity.this,"Please choose a future date", Toast.LENGTH_SHORT).show();
            }else if(dayEnd<dayStart){
                Toast.makeText(TestingActivity.this,"Please choose a future date", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(TestingActivity.this,"Period set", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
