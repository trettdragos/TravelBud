package com.example.dragostrett.tripbud;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    int mNotificationId = 001;
    static  Context context;
    public static String pass="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        username = (EditText)findViewById(R.id.editText_name);
        password = (EditText)findViewById(R.id.editText_password);
        fa=this;
        context = this;
    }

    public void loginUser(View view) throws InterruptedException {
        if(isOnline()){
            String ps=password.getText().toString(), un=username.getText().toString();
            pass=ps;
            UserInfo.setLogedIn(false);
            new loginBG(this).execute(un, ps);
        }
        else{
            Toast.makeText(this, "No internet acces",
                    Toast.LENGTH_SHORT).show();
        }


        /*NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.x)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setOngoing(true)
                        .setAutoCancel(false)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_ALL);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());*/
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
    public void cancelNotification()
    {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(mNotificationId);
    }
}
