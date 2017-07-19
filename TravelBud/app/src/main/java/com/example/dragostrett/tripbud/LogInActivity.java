package com.example.dragostrett.tripbud;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dragostrett.tripbud.Background.loginBG;
import com.example.dragostrett.tripbud.BasicInfo.TripInfo;
import com.example.dragostrett.tripbud.BasicInfo.UserInfo;

import java.sql.Date;
import java.util.Calendar;

public class LogInActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "RemeberMeFile";
    EditText username;
    EditText password;
    static int mNotificationId = 001;
    static  Context context;
    public static String pass="";
    CheckBox rememberMe, loggedIn ;
    public static SharedPreferences pref, prefAutoLogIn;
    public static Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        username = (EditText)findViewById(R.id.editText_name);
        password = (EditText)findViewById(R.id.editText_password);
        pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String u=pref.getString("username", null);
        String p = pref.getString("password", null);
        Boolean autoLogIn=pref.getBoolean("autoLogIn", false);
        username.setText(u);
        password.setText(p);
        if(autoLogIn){
            if(UserInfo.isAutoLogIn()){
                UserInfo.setLogedIn(false);
                new loginBG(this).execute(u, p);
            }else{
                pref.edit().putString("username", UserInfo.getUsername()).putString("password", UserInfo.getPassword()).putBoolean("autoLogIn", false).commit();
            }
        }
        fa=this;
        context = this;
        rememberMe=(CheckBox)findViewById(R.id.checkBoxRemember);
        loggedIn=(CheckBox)findViewById(R.id.checkBoxLoggedIn);
        loggedIn.setChecked(true);
        rememberMe.setChecked(true);
        cancelNotification();
        /*NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.x)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setOngoing(true)
                        .setAutoCancel(false)
                        .setPriority(Notification.PRIORITY_MIN)
                        .setDefaults(Notification.DEFAULT_ALL);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());*/
        cal= Calendar.getInstance();
        UserInfo.setCurentDate(new Date(cal.get(Calendar.YEAR)-1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
        TripInfo.setStartDate(new Date(0, 0, 0));
        TripInfo.setEndDate(new Date(0, 0, 0));
        UserInfo.setShowEveryThing(false);
    }

    public void loginUser(View view) throws InterruptedException {
        if(isOnline()){
            if(username.getText().equals("") || password.getText().equals("")){
                Toast.makeText(this, "Please enter username and password",
                        Toast.LENGTH_SHORT).show();
                return;
            } String ps=password.getText().toString(), un=username.getText().toString();
            if(rememberMe.isChecked()) {
                if(loggedIn.isChecked())
                pref.edit().putString("username", un).putString("password", ps).putBoolean("autoLogIn", true).commit();
                else pref.edit().putString("username", un).putString("password", ps).putBoolean("autoLogIn", false).commit();
            }else if(!pref.getString("username", null).equals(un)){
                pref.edit().putString("username", "").putString("password", "").putBoolean("autoLogIn", false).commit();
            }
            pass=ps;
            UserInfo.setLogedIn(false);
            new loginBG(this).execute(un, ps);
        }
        else{
            Toast.makeText(this, "No internet acces",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void openRegisterAct(View view){
        if(isOnline()){
            Intent intent = new Intent(this, RegisterActivity.class);
            this.startActivity(intent);
        }
        else{
            Toast.makeText(this, "No internet acces",
                    Toast.LENGTH_SHORT).show();
        }

        //cancelNotification();

    }
    public static Activity fa;
    public boolean isOnline(){
        ConnectivityManager c=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=c.getActiveNetworkInfo();
        return info !=null && info.isConnectedOrConnecting();
    }
    public static void cancelNotification()
    {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(mNotificationId);
    }
}
